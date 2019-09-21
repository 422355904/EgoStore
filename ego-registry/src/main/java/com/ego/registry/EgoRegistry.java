package com.ego.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Author TheKing
 * @Date 2019/9/21 16:20
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@SpringBootApplication
@EnableEurekaServer
public class EgoRegistry {

    public static void main(String[] args) {
        SpringApplication.run(EgoRegistry.class, args);
    }
}
