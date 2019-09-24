package com.ego.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author TheKing
 * @Date 2019/9/21 16:36
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.ego.item.mapper")
public class EgoItemService {

    public static void main(String[] args) {
        SpringApplication.run(EgoItemService.class, args);
    }
}
