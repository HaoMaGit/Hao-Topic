package com.hao.topic.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * Description: 系统管理服务
 * Author: Hao
 * Date: 2025/4/2 19:15
 */
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {
        "com.hao.topic.common.security"  // 添加这行，扫描security模块
})
public class TopicSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(TopicSystemApplication.class, args);
        System.out.println("  _   _             \n" +
                " | | | | __ _  ___  \n" +
                " | |_| |/ _` |/ _ \\ \n" +
                " |  _  | (_| | (_) |\n" +
                " |_| |_|\\__,_|\\___/ \n" +
                " >>> 系统服务启动成功 <<<\n");
    }
}
