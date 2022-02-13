package com.tianle.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: Tianle
 * @Date: 2021/11/23 15:24
 * @Description:
 */
@ComponentScan(basePackages = {"com.tianle"})
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient   //Nacos注册
public class OssApplication {
    public static void main(String[] args) {
        SpringApplication.run(OssApplication.class,args);
    }
}
