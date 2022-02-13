package com.tianle.vodservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: Tianle
 * @Date: 2022/1/1 17:41
 * @Description:
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"com.tianle"})
@EnableDiscoveryClient  //Nacos 注册
public class VodApplication {

    public static void main(String[] args) {
        SpringApplication.run(VodApplication.class,args);
    }
}
