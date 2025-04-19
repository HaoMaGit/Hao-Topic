package com.hao.topic.ai;

import com.hao.topic.common.config.MyMetaObjectHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Import;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/18 22:21
 */
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScans({
        @ComponentScan("com.hao.topic.common.security")
        , @ComponentScan("com.hao.topic.common.handler"),
})
@Import(MyMetaObjectHandler.class)  // 直接导入配置类
public class TopicAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TopicAiApplication.class, args);
        System.out.println("  _   _             \n" +
                " | | | | __ _  ___  \n" +
                " | |_| |/ _` |/ _ \\ \n" +
                " |  _  | (_| | (_) |\n" +
                " |_| |_|\\__,_|\\___/ \n" +
                " >>> AI服务启动成功 <<<\n");
    }
}
