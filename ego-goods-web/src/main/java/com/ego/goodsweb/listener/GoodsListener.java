package com.ego.goodsweb.listener;

import com.ego.goodsweb.service.GoodsHtmlService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author TheKing
 * @Date 2019/10/9 16:57
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Component
public class GoodsListener {

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    /**
     * 利用RabbitMQ 同步静态化页面
     * @param spuid
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ego.update.web.queue", durable = "true"),
            exchange = @Exchange(
                    value = "ego.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}))
    public void listenUpdate(Long spuid){
        if (spuid == null) {
            return;
        }
        // 创建页面
        goodsHtmlService.createHtml(spuid);
    }

}
