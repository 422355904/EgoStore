package com.ego.user.service;

import com.ego.common.utils.CodecUtils;
import com.ego.common.utils.NumberUtils;
import com.ego.user.mapper.UserMapper;
import com.ego.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @Author TheKing
 * @Date 2019/10/10 14:42
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Service
@Slf4j
public class UserSerive implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     *
     * @param data
     * @param type
     * @return
     */
    public Boolean checkData(String data, Integer type) {
        User user = new User();
        if (type==1){ //校验用户名
            user.setUsername(data);
        }
        else if (type==2){//校验电话号码，并发送短信验证码
            user.setPhone(data);
        }
        return userMapper.selectOne(user)==null?true:false;
    }

    /**
     * 发送手机短信验证码
     * @param phone
     */
    public void sendSms(String phone) {
        //1.生成验证码
        String code = NumberUtils.generateCode(6);
        //2.保存验证码到redis,五分钟失效
        stringRedisTemplate.opsForValue().set(phone, code,5, TimeUnit.MINUTES);
        //3.将任务添加到RabbitMQ队列，异步发送短信
            //拼接好数据，发送给接收方
        StringBuilder phoneCode=new StringBuilder();
        phoneCode.append(phone+"&&&"+code);

            //设置RabbitMQ发送失败回调setConfirmCallback
        Message message = MessageBuilder.withBody(phoneCode.toString().getBytes()).setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding("utf-8").setMessageId(phoneCode.toString()).build();
            //回调标识
        CorrelationData correlationData = new CorrelationData(phone);
            //一个rabbitTemplate只能有一个回调
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(this);

        rabbitTemplate.convertAndSend("sms.verify.code",message,correlationData);
    }

    /**
     * 注册用户
     * @param user
     * @param code
     * @return
     */
    @Transactional
    public Boolean register(User user, String code) {
        boolean result=false;
        //取出Redis中的验证码，比对用户填写的验证码
        String codeInRedis = stringRedisTemplate.opsForValue().get(user.getPhone());
        if (StringUtils.isNotBlank(code)&&StringUtils.isNotBlank(codeInRedis)&&code.equals(codeInRedis)){
            //保存用户
            user.setCreated(LocalDateTime.now());
            //给数据库用户密码加密
            String passwordEncode = CodecUtils.passwordBcryptEncode(user.getUsername(), user.getPassword());
            user.setPassword(passwordEncode);
            userMapper.insert(user);
            result=true; //保存成功
        }
        return result;
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        log.info("MQ成功接收到手机号[{}]消息",correlationData.getId());
    }

    public User queryUser(String username, String password) {
        User user=new User();
        user.setUsername(username);
        //根据表单填写的账号查询到用户
        User userInDB = userMapper.selectOne(user);
        if (null==userInDB){
            return null;
        }
        //判断表单密码是否正确
        Boolean result = CodecUtils.passwordConfirm(username + password,userInDB.getPassword());

        return result?userInDB:null;
    }
}
