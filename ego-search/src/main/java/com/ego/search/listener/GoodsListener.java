package com.ego.search.listener;

import com.ego.item.pojo.Brand;
import com.ego.item.pojo.Category;
import com.ego.item.pojo.SpuBo;
import com.ego.search.pojo.Goods;
import com.ego.search.repository.GoodsRepository;
import com.ego.search.service.SearchService;
import com.ego.search.web.client.BrandClient;
import com.ego.search.web.client.CategoryClient;
import com.ego.search.web.client.GoodsClient;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @Author TheKing
 * @Date 2019/10/9 16:23
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Component
public class GoodsListener {

    @Autowired
    private SearchService searchService;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;

    /**
     * RabbitMQ 处理insert和update的消息
     * @param spuid
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ego.update.index.queue", durable = "true"),
            exchange = @Exchange(
                    value = "ego.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}))
    public void listenUpdate(Long spuid) throws Exception{
        if (spuid == null) {
            return;
        }
        // 更新ES中数据
        SpuBo spuBo = goodsClient.queryGoodsById(spuid);
        Goods goods =searchService.buildGoods(spuBo);
        goodsRepository.save(goods);
    }




}
