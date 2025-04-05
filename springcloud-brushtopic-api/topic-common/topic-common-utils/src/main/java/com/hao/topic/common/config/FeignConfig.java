package com.hao.topic.common.config;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Feign 客户端配置类。
 * 该配置类用于配置 Feign 客户端的相关设置，包括启用 Feign 客户端和配置 HTTP 消息转换器。
 *
 * @author hao
 */
@Configuration
@EnableFeignClients
public class FeignConfig {

    /**
     * 配置 Feign 客户端使用的 HTTP 消息转换器。
     * 使用 {@link MappingJackson2HttpMessageConverter} 来处理 JSON 格式的数据转换。
     *
     * @return 配置的 {@link HttpMessageConverters} 实例
     */
    @Bean
    public HttpMessageConverters feignHttpMessageConverters() {
        return new HttpMessageConverters(new MappingJackson2HttpMessageConverter());
    }
}
