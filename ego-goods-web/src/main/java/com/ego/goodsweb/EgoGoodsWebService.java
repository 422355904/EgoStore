package com.ego.goodsweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author TheKing
 * @Date 2019/10/8 20:53
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients
public class EgoGoodsWebService {

    public static void main(String[] args) {
        SpringApplication.run(EgoGoodsWebService.class,args);
    }

}
