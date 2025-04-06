package com.hao.topic.security.config;

import com.hao.topic.security.filter.CookieToHeadersFilter;
import com.hao.topic.security.handle.*;
import com.hao.topic.security.security.AuthenticationEntryPoint;
import com.hao.topic.security.security.SecurityRepository;
import com.hao.topic.security.service.SecurityUserDetailsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.DelegatingReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.LinkedList;

@Configuration
@Slf4j
@EnableWebFluxSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    // 用户信息类
    @Autowired
    SecurityUserDetailsService securityUserDetailsService;
    // 拒绝处理器
    @Autowired
    AccessDeniedHandler accessDeniedHandler;
    // 认证成功处理器
    @Autowired
    AuthenticationSuccessHandler authenticationSuccessHandler;
    // 处理认证失败
    @Autowired
    AuthenticationFailHandler authenticationFailHandler;
    // 校验权限
    @Autowired
    SecurityRepository securityRepository;
    // 将token添加到请求头中
    @Autowired
    CookieToHeadersFilter cookieToHeadersFilter;
    // 处理登出成功的情况
    @Autowired
    LogoutSuccessHandler logoutSuccessHandler;
    // 登出成功处理器
    @Autowired
    LogoutHandler logoutHandler;
    // 处理未认证
    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;
    @Resource
    private SecurityConfig securityConfig;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(securityRepository)
                .authenticationManager(reactiveAuthenticationManager())  // 添加认证管理器
                .addFilterBefore(cookieToHeadersFilter, SecurityWebFiltersOrder.HTTP_HEADERS_WRITER)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/security/user/login").permitAll()  // 登录接口不需要权限
                        .pathMatchers("/system/captcha").permitAll()       // 验证码接口不需要权限
                        .pathMatchers("/security/user/logout").permitAll()       // 退出接口不需要权限

                        .anyExchange().authenticated()                     // 其他所有接口都需要认证
                )
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .logout(logoutSpec -> logoutSpec
                        .logoutUrl("/security/user/logout")
                        .logoutHandler(logoutHandler)
                        .logoutSuccessHandler(logoutSuccessHandler)
                );

        return http.build();
    }


    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        LinkedList<ReactiveAuthenticationManager> managers = new LinkedList<>();
        UserDetailsRepositoryReactiveAuthenticationManager userDetailsManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(securityUserDetailsService);
        userDetailsManager.setPasswordEncoder(securityConfig.passwordEncoder());
        managers.add(userDetailsManager);

        // 添加一个处理已认证用户的管理器
        managers.add(authentication -> {
            if (authentication.isAuthenticated()) {
                return Mono.just(authentication);
            }
            return Mono.empty();
        });

        return new DelegatingReactiveAuthenticationManager(managers);
    }
}
 
 
 
 