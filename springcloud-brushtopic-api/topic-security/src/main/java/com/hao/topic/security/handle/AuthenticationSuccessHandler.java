package com.hao.topic.security.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.utils.JWTUtils;
import com.hao.topic.security.constant.JwtConstant;
import com.hao.topic.security.properties.AuthProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * 自定义认证成功处理器，用于处理用户认证成功的情况
 * <p>
 * 该类继承自 {@link WebFilterChainServerAuthenticationSuccessHandler}，用于在Spring Security中处理用户认证成功后的操作。
 * 当用户成功登录时，将调用此处理器。
 * 它会生成JWT令牌，并将其存储在Redis中，同时将令牌作为cookie返回给客户端。
 *
 * @author hao
 */
@Component
@Slf4j
public class AuthenticationSuccessHandler extends WebFilterChainServerAuthenticationSuccessHandler {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AuthProperties ignoreWhiteProperties;

    /**
     * 处理用户认证成功的情况
     * <p>
     * 当用户成功登录时，将调用此方法。
     * 该方法会生成JWT令牌，并将其存储在Redis中，同时将令牌作为cookie返回给客户端。
     *
     * @param webFilterExchange 当前的 {@link WebFilterExchange} 对象
     * @param authentication    当前认证的 {@link Authentication} 对象
     * @return {@link Mono<Void>} 表示处理完成
     */
    @SneakyThrows
    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        // 获取当前的ServerWebExchange对象
        ServerWebExchange exchange = webFilterExchange.getExchange();
        // 获取响应对象
        ServerHttpResponse response = exchange.getResponse();

        // 设置响应头
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");

        // 设置响应体
        HashMap<String, String> map = new HashMap<>();
        // 获取请求头中的"Remember-me"字段
        String remember_me = exchange.getRequest().getHeaders().getFirst("Remember-me");

        ObjectMapper mapper = new ObjectMapper();
        // 获取用户的权限列表
        List<? extends GrantedAuthority> list = authentication.getAuthorities().stream().toList();
        try {
            // 创建包含用户名和角色的负载
            Map<String, String> load = new HashMap<>();
            load.put("username", authentication.getName());
            load.put("role", list.get(0).getAuthority()); // 这里只添加了一种角色，实际上用户可以有不同的角色类型

            String token;
            log.info(authentication.toString());

            // 根据"Remember-me"字段生成不同的JWT令牌
            if (remember_me == null) {
                // 如果没有"Remember-me"字段，生成一个有效期为24小时的令牌
                token = JWTUtils.creatToken(load, JwtConstant.EXPIRE_TIME * ignoreWhiteProperties.getTimeout());
                response.addCookie(ResponseCookie.from("token", token).path("/").build());
                // maxAge默认-1，浏览器关闭cookie失效
                redisTemplate.opsForValue().set(authentication.getName(), token, ignoreWhiteProperties.getTimeout(), TimeUnit.DAYS);
            } else {
                // 如果有"Remember-me"字段，生成一个有效期为180天的令牌
                token = JWTUtils.creatToken(load, JwtConstant.EXPIRE_TIME * ignoreWhiteProperties.getRememberMe());
                // 设置cookie的maxAge为180天
                response.addCookie(ResponseCookie.from("token", token).maxAge(Duration.ofDays(ignoreWhiteProperties.getRememberMe())).path("/").build());
                // 保存180天
                redisTemplate.opsForValue().set(authentication.getName(), token, ignoreWhiteProperties.getRememberMe(), TimeUnit.DAYS);
            }

            // 设置响应体内容
            map.put("code", String.valueOf(ResultCodeEnum.SUCCESS.getCode()));
            map.put("message", ResultCodeEnum.SUCCESS.getMessage());
            map.put("token", token);
        } catch (Exception ex) {
            ex.printStackTrace();
            // 设置响应体内容为登录失败
            map.put("code", String.valueOf(ResultCodeEnum.FAIL.getCode()));
            map.put("message", ResultCodeEnum.FAIL.getMessage());
        }

        // 将响应体转换为JSON字节数组
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(mapper.writeValueAsBytes(map));
        // 将JSON字节数组写入响应体
        return response.writeWith(Mono.just(bodyDataBuffer));
    }
}
