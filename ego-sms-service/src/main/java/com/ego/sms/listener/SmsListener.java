package com.ego.sms.listener;

import com.ego.sms.utils.SmsUtils;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author TheKing
 * @Date 2019/10/10 16:02
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Component
public class SmsListener {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ego.sms.queue", durable = "true"),
            exchange = @Exchange(value = "ego.sms.exchange",
                    ignoreDeclarationExceptions = "true"),
            key = {"sms.verify.code"}))
    public void listenSms(Message message, Channel channel) throws Exception{
        //消费者取出数据
        String phoneCode=new String(message.getBody(),"utf-8");
        String[] split = phoneCode.split("&&&");
        String phone=split[0];
        String code=split[1];
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(code)) {
            // 放弃处理
            return;
        }
        //调用阿里大于短信平台发送短信
        SmsUtils.sendSms(phone,code);
        //手动确认ack，防止数据丢失
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
