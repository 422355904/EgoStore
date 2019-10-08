package com.ego.search;

import com.ego.common.pojo.PageResult;
import com.ego.item.pojo.SpuBo;
import com.ego.search.pojo.Goods;
import com.ego.search.repository.GoodsRepository;
import com.ego.search.service.SearchService;
import com.ego.search.web.client.GoodsClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author TheKing
 * @Date 2019/9/29 16:27
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@SpringBootTest(classes = EgoSearchService.class)
@RunWith(SpringRunner.class)
public class CategoryClientTest {

    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private SearchService searchService;
    @Autowired
    private GoodsRepository goodsRepository;

    /**
     * 测试连接ES，并新建索引
     */
    @Test
    public void importData() {
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);
    }

    /**
     * 测试数据搬移
     */
    @Test
    public void test2() {
        int size = 100;
        int count = 0;
        int pageNo = 1;
        do {
            //分页查询数据
            PageResult<SpuBo> pageResult = goodsClient.getGoodsByPage(pageNo, size, null, null, "", true).getBody();

            List<SpuBo> items = pageResult.getItems();

            count = items.size();

            List<Goods> goodsList = new ArrayList<>(count);

            //将SpuBO-->Goods
            for (SpuBo spuBO : items) {
                try {
                    Goods goods = searchService.buildGoods(spuBO);
                    goodsList.add(goods);
                } catch (Exception e) {
                    System.out.println("spu:" + spuBO.getId() + "转换出现问题");
                    e.printStackTrace();
                }

            }
            goodsRepository.saveAll(goodsList);
            pageNo++;
        } while (count == 100);

    }
}
