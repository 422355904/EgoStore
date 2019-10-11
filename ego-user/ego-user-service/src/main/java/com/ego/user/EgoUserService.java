package com.ego.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author TheKing
 * @Date 2019/10/10 14:01
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.ego.user.mapper")
public class EgoUserService {
    public static void main(String[] args) {
        SpringApplication.run(EgoUserService.class, args);
    }
}
