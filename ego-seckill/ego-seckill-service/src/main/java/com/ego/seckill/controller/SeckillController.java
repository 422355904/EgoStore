package com.ego.seckill.controller;


import com.ego.auth.pojo.UserInfo;
import com.ego.seckill.access.AccessLimit;
import com.ego.seckill.client.GoodsClient;
import com.ego.seckill.interceptor.LoginInterceptor;
import com.ego.seckill.vo.SeckillGoods;
import com.ego.seckill.vo.SeckillMessage;
import com.ego.seckill.service.SeckillService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: yaorange
 * @Time: 2019-03-30 16:57
 * @Feature:
 */
@RestController
@RequestMapping
public class SeckillController implements InitializingBean {

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    private static final String KEY_PREFIX = "ego:seckill:stock";

    private Map<Long,Boolean> localOverMap = new HashMap<>();

    /**
     * 系统初始化，初始化秒杀商品数量
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        //1.查询可以秒杀的商品
        BoundHashOperations<String,Object,Object> hashOperations = this.stringRedisTemplate.boundHashOps(KEY_PREFIX);
        hashOperations.entries().forEach((m,n) ->
                localOverMap.put(Long.parseLong(m.toString()),false)
        );
    }


    @GetMapping("/list")
    public ResponseEntity<List<SeckillGoods>> list()
    {
        return ResponseEntity.ok(seckillService.list());
    }
    @GetMapping("/{id}")
    public ResponseEntity<SeckillGoods> queryById(@PathVariable("id") Long id)
    {
        return ResponseEntity.ok(seckillService.queryById(id));
    }




    /**
     * 创建秒杀路径
     * @param goodsId
     * @return
     */
    @AccessLimit(seconds = 20,maxCount = 5,needLogin = true)
    @GetMapping("get_path/{goodsId}/{skuId}")
    public ResponseEntity<String> getSeckillPath(@PathVariable("goodsId") Long goodsId,@PathVariable("skuId") Long skuId){
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        if (userInfo == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        BoundHashOperations<String,Object,Object> hashOperations = this.stringRedisTemplate.boundHashOps(KEY_PREFIX);

        Long stock = Long.valueOf(hashOperations.get(skuId.toString()).toString());
        //4.库存不足直接返回
        if (stock <= 0 ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String str = this.seckillService.createPath(goodsId,userInfo.getId());
        if (StringUtils.isEmpty(str)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(str);
    }

    /**
     * 秒杀
     * @param path
     * @param seckillGoods
     * @return
     */
    @PostMapping("/{path}/seck")
    public ResponseEntity<Long> seckillOrder(@PathVariable("path") String path, @RequestBody SeckillGoods seckillGoods){

        UserInfo userInfo = LoginInterceptor.getLoginUser();

        //1.验证路径
        boolean check = this.seckillService.checkSeckillPath(seckillGoods.getId(),userInfo.getId(),path);
        if (!check){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //2.内存标记，减少redis访问
        Boolean over = localOverMap.get(seckillGoods.getSkuId());
        if (over!=null && over){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //3.读取库存，减一后更新缓存
        BoundHashOperations<String,Object,Object> hashOperations = this.stringRedisTemplate.boundHashOps(KEY_PREFIX);

        Long stock = Long.valueOf(hashOperations.get(seckillGoods.getSkuId().toString()).toString());
        //4.库存不足直接返回
        if (stock <= 0 ){
            localOverMap.put(seckillGoods.getSkuId(),true);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //5.设置分布式锁条件
        String lockKey = "buying_" + seckillGoods.getSkuId();
        Boolean canBuy = stringRedisTemplate.execute((RedisConnection conn) -> {
            try {
                return conn.setNX(stringRedisTemplate.getStringSerializer().serialize(lockKey),
                        stringRedisTemplate.getStringSerializer().serialize("1"));
            } finally {
                conn.close();
            }
        });
        //如果不能买，说明有人正在并发购买,返回404
        if(!canBuy)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //6.库存充足，减redis库存，请求入队
        hashOperations.increment(seckillGoods.getSkuId().toString(), -1);

        //6.1 获取用户信息
        SeckillMessage seckillMessage = new SeckillMessage(userInfo,seckillGoods);
        //6.2 发送消息
        this.seckillService.sendMessage(seckillMessage);
        //7.删除分布式锁条件
        stringRedisTemplate.delete(lockKey);
        return ResponseEntity.ok(userInfo.getId());
    }

    /**
     * 根据userId查询订单号
     * @param userId
     * @return
     */
    @GetMapping("/checkSeckillOrder/{userId}")
    public ResponseEntity<Long> checkSeckillOrder(@PathVariable("userId") Long userId){
        Long result = this.seckillService.checkSeckillOrder(userId);
        if (result == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);

    }
}
