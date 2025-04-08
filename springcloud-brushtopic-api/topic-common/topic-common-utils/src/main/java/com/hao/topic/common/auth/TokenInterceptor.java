package com.hao.topic.common.auth;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * Feign 客户端请求拦截器，用于在每个 Feign 请求中添加 Authorization 头。
 * 该拦截器通过 ThreadLocal 存储和传递 token，确保在 Feign 请求中能够正确传递认证信息。
 *
 * @author hao
 */
@Component
public class TokenInterceptor implements RequestInterceptor {

    /**
     * ThreadLocal 用于存储当前线程的 token。
     * 使用 ThreadLocal 可以确保每个线程都有自己独立的 token 副本。
     */
    private static final ThreadLocal<String> tokenThreadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程的 token。
     *
     * @param token 要设置的 token 字符串
     */
    public static void setToken(String token) {
        tokenThreadLocal.set(token);
    }

    /**
     * 清除当前线程的 token。
     * 在请求处理完成后，建议清除 token 以避免潜在的线程安全问题。
     */
    public static void clearToken() {
        tokenThreadLocal.remove();
    }

    /**
     * Feign 请求拦截器方法，用于在每个 Feign 请求中添加 Authorization 头。
     *
     * @param requestTemplate Feign 请求模板，用于构建和修改请求
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 从 ThreadLocal 中获取当前线程的 token
        String token = tokenThreadLocal.get();
        System.out.println("token:" + token);
        if (token != null) {
            // 将 token 添加到请求头中
            requestTemplate.header("Authorization", token);
        }
    }
}
