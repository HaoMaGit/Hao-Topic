package com.hao.topic.system;

import com.hao.topic.common.config.MyMetaObjectHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Import;

/**
 * Description: 系统管理服务
 * Author: Hao
 * Date: 2025/4/2 19:15
 */
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScans({
        @ComponentScan("com.hao.topic.common.security")
        , @ComponentScan("com.hao.topic.common.handler"),
})
@Import(MyMetaObjectHandler.class)  // 直接导入配置类
@EnableFeignClients(basePackages = {"com.hao.topic.client.system", "com.hao.topic.client.security"})
@ComponentScan("com.hao.topic.api.utils")
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
