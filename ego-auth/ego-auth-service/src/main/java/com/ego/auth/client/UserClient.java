package com.ego.auth.client;

import com.ego.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author TheKing
 * @Date 2019/10/11 16:24
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@FeignClient(value = "user-service")
public interface UserClient extends UserApi {
}
