package com.hao.topic.common.security.config;

import com.hao.topic.common.security.filter.JwtAuthenticationFilter;
import com.hao.topic.common.security.handler.AccessDeniedHandlerImpl;
import com.hao.topic.security.handle.AuthenticationFailHandler;
import com.hao.topic.security.security.AuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Spring Security配置类，用于配置安全相关的设置。
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * 注入自定义的访问拒绝处理器。
     */
    @Autowired
    private AccessDeniedHandlerImpl authenticationFailHandler;

    /**
     * 配置Spring Security的过滤器链。
     *
     * @param http HttpSecurity对象，用于配置安全设置
     * @return 配置好的SecurityFilterChain对象
     * @throws Exception 如果配置过程中发生异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 配置HTTP请求的授权规则
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/system/captchaImage").permitAll()
                        .requestMatchers("/system/**").permitAll()
                        .anyRequest().authenticated()
                )
                // 添加自定义的JWT认证过滤器，在UsernamePasswordAuthenticationFilter之前执行
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // 配置异常处理逻辑
                .exceptionHandling(ex -> ex
                        // 设置认证失败时的入口点处理器
                        .authenticationEntryPoint(authenticationFailHandler)
                );
        // 构建并返回SecurityFilterChain对象
        return http.build();
    }

    /**
     * 创建并返回JWT认证过滤器实例。
     *
     * @return JwtAuthenticationFilter实例
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}
