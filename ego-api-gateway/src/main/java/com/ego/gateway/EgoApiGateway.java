package com.ego.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @Author TheKing
 * @Date 2019/9/21 16:24
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@SpringCloudApplication
@EnableZuulProxy
@EnableDiscoveryClient
public class EgoApiGateway {

    public static void main(String[] args) {
        SpringApplication.run(EgoApiGateway.class, args);
    }
}
