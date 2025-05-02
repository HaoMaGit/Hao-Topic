package com.hao.topic.web.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;


/**
 * 跨域配置
 */
@Configuration
public class CorsConfig {

    /**
     * 配置Spring Security的HTTP安全配置
     *
     * @param http
     * @return
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }

    /**
     * 配置跨域请求过滤器
     * <p>
     * 跨域资源共享（CORS）配置是现代Web服务中常见需求此方法用于创建一个CorsWebFilter bean，
     * 该bean基于Spring框架，用于处理跨域请求问题通过允许所有来源、所有请求方法和所有请求头，
     * 此配置提供了一种灵活的方式，使得不同域下的客户端能够安全地进行请求
     *
     * @return CorsWebFilter 返回配置好的CorsWebFilter实例，用于处理跨域请求
     */
    @Bean
    public CorsWebFilter corsFilter() {
        // 创建一个新的CORS配置实例
        CorsConfiguration config = new CorsConfiguration();
        // 允许所有请求方法
        config.addAllowedMethod("*");
        // 允许所有来源
        config.addAllowedOrigin("*");
        // 允许所有请求头
        config.addAllowedHeader("*");

        // 创建一个新的URL路径模式解析器，用于匹配请求路径
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        // 将CORS配置注册到所有路径
        source.registerCorsConfiguration("/**", config);

        // 返回一个新的CorsWebFilter实例
        return new CorsWebFilter(source);
    }
}
