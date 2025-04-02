package com.hao.topic.security.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hao.topic.common.enums.ResultCodeEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;

/**
 * 自定义访问拒绝处理器，用于处理访问被拒绝的情况
 * <p>
 * 该类实现了 {@link ServerAccessDeniedHandler} 接口，用于在Spring Security中处理访问被拒绝的请求。
 * 当用户尝试访问需要更高权限的资源时，将调用此处理器。
 * 它将返回一个JSON格式的响应，状态码为403 Forbidden，并包含错误信息。
 *
 * @author hao
 */
@Slf4j
@Component
public class AccessDeniedHandler implements ServerAccessDeniedHandler {

    /**
     * 处理访问被拒绝的请求
     * <p>
     * 当用户尝试访问需要更高权限的资源时，将调用此方法。
     * 该方法设置响应的状态码为403 Forbidden，并返回一个JSON格式的错误信息。
     *
     * @param exchange 当前的 {@link ServerWebExchange} 对象
     * @param denied   访问被拒绝的异常
     * @return {@link Mono<Void>} 表示处理完成
     */
    @SneakyThrows
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        // 获取响应对象
        ServerHttpResponse response = exchange.getResponse();
        // 设置响应状态码为403 Forbidden
        response.setStatusCode(HttpStatus.FORBIDDEN);
        // 设置响应头，指定内容类型为JSON
        response.getHeaders().add("Content-Type", "application/json; charset=UTF-8");

        // 创建错误信息的JSON对象
        HashMap<String, String> map = new HashMap<>();
        map.put("code", String.valueOf(ResultCodeEnum.LOGIN_ERROR_SECURITY.getCode()));
        map.put("message", ResultCodeEnum.LOGIN_ERROR_SECURITY.getMessage());

        // 记录错误日志，包含被拒绝访问的路径
        log.error("access forbidden path={}", exchange.getRequest().getPath());

        // 使用ObjectMapper将错误信息转换为JSON字节数组
        ObjectMapper objectMapper = new ObjectMapper();
        DataBuffer dataBuffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(map));

        // 将JSON字节数组写入响应体
        return response.writeWith(Mono.just(dataBuffer));
    }
}
