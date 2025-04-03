package com.hao.topic.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * Description: 认证服务
 * Author: Hao
 * Date: 2025/4/1 10:32
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {
        "com.hao.topic.common.handler"  // 添加这一行，扫描通用模块的异常处理器
})
public class TopicSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(TopicSecurityApplication.class, args);
        System.out.println("  _   _             \n" +
                " | | | | __ _  ___  \n" +
                " | |_| |/ _` |/ _ \\ \n" +
                " |  _  | (_| | (_) |\n" +
                " |_| |_|\\__,_|\\___/ \n" +
                " >>> 认证服务启动成功 <<<\n");
    }
}
