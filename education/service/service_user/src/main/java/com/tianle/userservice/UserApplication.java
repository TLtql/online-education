package com.tianle.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: Tianle
 * @Date: 2022/1/8 14:36
 * @Description:
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.tianle"})
@EnableDiscoveryClient   //Nacos注册
@EnableFeignClients  // 服务调用
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
    }
}
