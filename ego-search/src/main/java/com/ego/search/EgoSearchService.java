package com.ego.search;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author TheKing
 * @Date 2019/9/29 13:38
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients
public class EgoSearchService {

    public static void main(String[] args) {
        SpringApplication.run(EgoSearchService.class, args);
    }
}
