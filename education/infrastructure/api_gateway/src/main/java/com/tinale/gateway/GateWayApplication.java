package com.tinale.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author: Tianle
 * @Date: 2022/2/9 15:36
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients  // 服务调用
public class GateWayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication.class, args);
    }
}
