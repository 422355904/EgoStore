package com.ego.cart.web.client;

import com.ego.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author TheKing
 * @Date 2019/10/12 17:29
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {

}