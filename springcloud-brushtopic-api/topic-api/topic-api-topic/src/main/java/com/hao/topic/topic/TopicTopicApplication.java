package com.hao.topic.topic;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Description: 题目管理服务
 */
@SpringBootApplication
@EnableDiscoveryClient
public class TopicTopicApplication {
    public static void main(String[] args) {
        SpringApplication.run(TopicTopicApplication.class, args);
        System.out.println("  _   _             \n" +
                " | | | | __ _  ___  \n" +
                " | |_| |/ _` |/ _ \\ \n" +
                " |  _  | (_| | (_) |\n" +
                " |_| |_|\\__,_|\\___/ \n" +
                " >>> 题目管理服务启动成功 <<<\n");
    }
}
