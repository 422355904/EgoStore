package com.ego.upload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author TheKing
 * @Date 2019/9/25 15:37
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EgoUploadService {

    public static void main(String[] args) {
        SpringApplication.run(EgoUploadService.class,args);
    }
}
