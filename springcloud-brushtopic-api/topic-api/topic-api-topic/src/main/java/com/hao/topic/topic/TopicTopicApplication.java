package com.hao.topic.topic;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Description:
 */
@EnableDiscoveryClient
@SpringBootApplication
public class TopicTopicApplication {
    public static void main(String[] args) {
        SpringApplication.run(TopicTopicApplication.class, args);
    }
}
