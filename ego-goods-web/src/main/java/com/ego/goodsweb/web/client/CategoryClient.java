package com.ego.goodsweb.web.client;

import com.ego.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author TheKing
 * @Date 2019/9/29 16:24
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {

}
