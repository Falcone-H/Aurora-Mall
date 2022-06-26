package com.besscroft.aurora.mall.canal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @description: TODO
 * @author: Falcone
 * @date: 2022/6/26 11:36
 */

@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
public class MallCanalApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallCanalApplication.class, args);
        log.info("  极光商城 Canal 中心启动成功  \n");
    }
}
