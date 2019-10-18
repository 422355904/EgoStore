package com.ego.cart.web.listener;

import com.ego.cart.service.CartService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author TheKing
 * @Date 2019/10/9 16:57
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Component
public class CartListener {

    @Autowired
    private CartService cartService;

    /**
     * 利用RabbitMQ 购物车系统删除已购买的商品
     * @param map
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ego.update.web.queue", durable = "true"),
            exchange = @Exchange(
                    value = "ego.cart.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"cart.delete"}))
    public void listenUpdate(Map map){
        if (map == null) {
            return;
        }
        // 创建页面
        cartService.deleteCart(map);
    }

}
