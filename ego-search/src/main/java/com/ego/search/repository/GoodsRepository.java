package com.ego.search.repository;

import com.ego.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author TheKing
 * @Date 2019/9/29 17:24
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Repository
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
