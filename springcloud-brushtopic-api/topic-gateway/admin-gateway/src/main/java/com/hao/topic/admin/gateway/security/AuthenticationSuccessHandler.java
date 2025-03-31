package com.hao.topic.admin.gateway.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hao.topic.admin.gateway.utils.JWTUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class AuthenticationSuccessHandler extends WebFilterChainServerAuthenticationSuccessHandler {
    // @Value("${login.timeout}")
    private int timeout = 3600;// 默认一小时
    private final int rememberMe = 180;
    @Autowired
    private RedisTemplate redisTemplate;

    @SneakyThrows
    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();
        // 设置headers
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        // 设置body
        HashMap<String, String> map = new HashMap<>();
        String remember_me = exchange.getRequest().getHeaders().getFirst("Remember-me");

        ObjectMapper mapper = new ObjectMapper();
        List<? extends GrantedAuthority> list = authentication.getAuthorities().stream().toList();
        try {
            Map<String, String> load = new HashMap<>();
            load.put("username", authentication.getName());
            load.put("role", list.get(0).getAuthority());// 这里只添加了一种角色 实际上用户可以有不同的角色类型
            String token;
            log.info(authentication.toString());
            if (remember_me == null) {
                token = JWTUtils.creatToken(load, 3600 * 24);
                response.addCookie(ResponseCookie.from("token", token).path("/").build());
                // maxAge默认-1 浏览器关闭cookie失效
                redisTemplate.opsForValue().set(authentication.getName(), token, 1, TimeUnit.DAYS);
            } else {
                token = JWTUtils.creatToken(load, 3600 * 24 * 180);
                response.addCookie(ResponseCookie.from("token", token).maxAge(Duration.ofDays(rememberMe)).path("/").build());
                redisTemplate.opsForValue().set(authentication.getName(), token, rememberMe, TimeUnit.SECONDS);// 保存180天
            }

            map.put("code", "000220");
            map.put("message", "登录成功");
            map.put("token", token);
        } catch (Exception ex) {
            ex.printStackTrace();
            map.put("code", "000440");
            map.put("message", "登录失败");
        }
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(mapper.writeValueAsBytes(map));
        return response.writeWith(Mono.just(bodyDataBuffer));
    }

}