package com.hao.topic.security.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hao.topic.common.enums.ResultCodeEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.authentication.HttpBasicServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;

/**
 * 自定义认证入口点，用于处理未认证的请求
 * <p>
 * 该类继承自 {@link HttpBasicServerAuthenticationEntryPoint}，用于在Spring Security中处理未认证的请求。
 * 当用户尝试访问需要认证的资源但未提供有效的认证信息时，将调用此入口点。
 * 它将返回一个JSON格式的响应，状态码为401 Unauthorized，并包含错误信息。
 *
 * @author your_name
 */
@Slf4j
@Component
public class AuthenticationEntryPoint extends HttpBasicServerAuthenticationEntryPoint {

    /**
     * 处理未认证的请求
     * <p>
     * 当用户尝试访问需要认证的资源但未提供有效的认证信息时，将调用此方法。
     * 该方法设置响应的状态码为401 Unauthorized，并返回一个JSON格式的错误信息。
     *
     * @param exchange 当前的 {@link ServerWebExchange} 对象
     * @param e        认证异常
     * @return {@link Mono<Void>} 表示处理完成
     */
    @SneakyThrows
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json; charset=UTF-8");
        // 创建错误信息的JSON对象
        HashMap<String, String> map = new HashMap<>();
        map.put("code", String.valueOf(ResultCodeEnum.LOGIN_ERROR.getCode()));
        map.put("message", ResultCodeEnum.LOGIN_ERROR.getMessage());
        // 使用ObjectMapper将错误信息转换为JSON字节数组
        ObjectMapper objectMapper = new ObjectMapper();
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(map));
        // 将JSON字节数组写入响应体
        return response.writeWith(Mono.just(bodyDataBuffer));
    }
}
