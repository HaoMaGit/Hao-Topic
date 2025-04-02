package com.hao.topic.security.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hao.topic.common.constant.ExceptionConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;

/**
 * 自定义认证失败处理器，用于处理认证失败的情况
 * <p>
 * 该类实现了 {@link ServerAuthenticationFailureHandler} 接口，用于在Spring Security中处理认证失败的请求。
 * 当用户尝试登录但认证失败时，将调用此处理器。
 * 它将返回一个JSON格式的响应，状态码为403 Forbidden，并包含错误信息。
 *
 * @author hao
 */
@Slf4j
@Component
public class AuthenticationFailHandler implements ServerAuthenticationFailureHandler {

    /**
     * 处理认证失败的请求
     * <p>
     * 当用户尝试登录但认证失败时，将调用此方法。
     * 该方法设置响应的状态码为403 Forbidden，并返回一个JSON格式的错误信息。
     *
     * @param webFilterExchange 当前的 {@link WebFilterExchange} 对象
     * @param e                 认证失败的异常
     * @return {@link Mono<Void>} 表示处理完成
     */
    @SneakyThrows
    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException e) {
        // 获取响应对象
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
        // 设置响应状态码为403 Forbidden
        response.setStatusCode(HttpStatus.FORBIDDEN);
        // 设置响应头，指定内容类型为JSON
        response.getHeaders().add("Content-Type", "application/json; charset=UTF-8");

        // 创建错误信息的JSON对象
        HashMap<String, String> map = new HashMap<>();
        map.put("code", String.valueOf(ExceptionConstant.AUTH_ERROR_CODE));
        map.put("message", e.getMessage());

        // 记录错误日志，包含认证失败的路径
        log.error("认证失败路径={}", webFilterExchange.getExchange().getRequest().getPath());

        // 使用ObjectMapper将错误信息转换为JSON字节数组
        ObjectMapper objectMapper = new ObjectMapper();
        DataBuffer dataBuffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(map));

        // 将JSON字节数组写入响应体
        return response.writeWith(Mono.just(dataBuffer));
    }
}
