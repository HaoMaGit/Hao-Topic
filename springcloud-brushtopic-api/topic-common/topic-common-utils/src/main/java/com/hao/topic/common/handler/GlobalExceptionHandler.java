package com.hao.topic.common.handler;

import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局异常处理器，用于捕获和处理应用程序中的各种异常。
 * 使用 @RestControllerAdvice 注解标识这是一个全局异常处理器。
 *
 * @Order(-1) 确保该处理器优先级最高。
 */
@Slf4j
@RestControllerAdvice
@Order(-1)
public class GlobalExceptionHandler {

    /**
     * 处理 UsernameNotFoundException 异常。
     * 当用户名未找到时，返回一个包含错误信息的结果。
     *
     * @param e        异常对象
     * @param exchange 当前的 ServerWebExchange 对象
     * @return 包含错误信息的 Mono<Result<String>>
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Result<String>> handleUsernameNotFoundException(UsernameNotFoundException e, ServerWebExchange exchange) {
        return Mono.just(Result.fail(e.getMessage()));
    }

    /**
     * 处理 BadCredentialsException 异常。
     * 当用户名或密码错误时，记录错误日志并返回一个包含错误信息的结果。
     *
     * @param e        异常对象
     * @param exchange 当前的 ServerWebExchange 对象
     * @return 包含错误信息的 Mono<Result<String>>
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Result<String>> handleBadCredentialsException(BadCredentialsException e, ServerWebExchange exchange) {
        log.error("用户名或密码错误: {}", e.getMessage());
        return Mono.just(Result.fail(ResultCodeEnum.PASSWORD_ERROR));
    }

    /**
     * 处理所有其他类型的异常。
     * 记录错误日志并返回一个包含错误信息的结果。
     *
     * @param e        异常对象
     * @param exchange 当前的 ServerWebExchange 对象
     * @return 包含错误信息的 Mono<Result<String>>
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Result<String>> handleException(Exception e, ServerWebExchange exchange) {
        log.error("系统异常: ", e);
        return Mono.just(Result.fail(e.getMessage()));
    }
}
