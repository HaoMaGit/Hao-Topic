package com.hao.topic.topic;


import com.hao.topic.api.utils.config.MybatisPlusConfig;
import com.hao.topic.common.config.MyMetaObjectHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

/**
 * Description: 题目管理服务
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScans({
        @ComponentScan("com.hao.topic.common.security")
        , @ComponentScan("com.hao.topic.common.handler"),
})
@Import({MyMetaObjectHandler.class, MybatisPlusConfig.class})  // 直接导入配置类
@ComponentScan({"com.hao.topic.api.utils", "com.hao.topic.common.auth"})
@EnableFeignClients
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
