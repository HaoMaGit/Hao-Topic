package com.hao.topic.admin.gateway.handler;

import com.hao.topic.common.utils.ServletUtils;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关统一异常处理
 * <p>
 * 该类实现了ErrorWebExceptionHandler接口，用于处理网关中的各种异常情况。
 * 它能够捕获并处理NotFoundException和ResponseStatusException，并记录详细的日志信息。
 * 对于其他类型的异常，它会返回一个通用的“内部服务器错误”消息。
 *
 * @author ruoyi
 */
@Order(-1)
@Configuration
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GatewayExceptionHandler.class);

    /**
     * 处理异常请求
     * <p>
     * 该方法会捕获并处理ServerWebExchange中的异常，并根据异常类型返回相应的错误信息。
     * 如果响应已经提交，则直接返回错误信息。
     *
     * @param exchange ServerWebExchange对象，包含请求和响应信息
     * @param ex       抛出的异常
     * @return Mono<Void> 表示异步处理完成
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        // 如果响应已经提交，则直接返回错误信息
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }

        String msg;

        // 根据异常类型设置不同的错误消息
        if (ex instanceof NotFoundException) {
            msg = "服务未找到";
        } else if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            msg = responseStatusException.getMessage();
        } else {
            msg = "内部服务器错误";
        }

        // 记录详细的日志信息
        log.error("请求路径:{},异常信息:{}", exchange.getRequest().getPath(), ex.getMessage());

        // 返回错误响应
        return ServletUtils.webFluxResponseWriter(response, msg);
    }
}
