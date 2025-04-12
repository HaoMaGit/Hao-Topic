package com.hao.topic.security.handle;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.hao.topic.common.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 自定义登出处理器，用于处理用户登出请求
 * <p>
 * 该类实现了 {@link ServerLogoutHandler} 接口，用于在Spring Security中处理用户登出操作。
 * 当用户发起登出请求时，将调用此处理器。
 * 它会从请求中获取JWT令牌，并从Redis中删除与该令牌关联的用户信息。
 *
 * @author hao
 */
@Component
@Slf4j
public class LogoutHandler implements ServerLogoutHandler {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 处理用户登出请求
     * <p>
     * 当用户发起登出请求时，将调用此方法。
     * 该方法会从请求中获取JWT令牌，并从Redis中删除与该令牌关联的用户信息。
     *
     * @param webFilterExchange 当前的 {@link WebFilterExchange} 对象
     * @param authentication    当前认证的 {@link Authentication} 对象
     * @return {@link Mono<Void>} 表示处理完成
     */
    @Override
    public Mono<Void> logout(WebFilterExchange webFilterExchange, Authentication authentication) {
        // 获取 ServerHttpResponse 对象，用于返回响应
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();

        // 从请求中获取名为 "token" 的cookie
        HttpCookie cookie = webFilterExchange.getExchange().getRequest().getCookies().getFirst("token");
        try {
            if (cookie != null) {
                // 解析JWT令牌，获取用户信息
                Map<String, Object> userMap = JWTUtils.getTokenInfo(cookie.getValue());
                // 从Redis中删除与该用户名关联的令牌
                redisTemplate.delete((String) userMap.get("username"));

                // 清理浏览器中的 token Cookie
                ResponseCookie deletedCookie = ResponseCookie.from("token", "")
                        .path("/") // 确保路径匹配
                        .httpOnly(true) // 如果原始 Cookie 是 HttpOnly，则这里也需要设置
                        .secure(true) // 如果原始 Cookie 是 Secure，则这里也需要设置
                        .maxAge(0) // 设置过期时间为 0，表示立即删除
                        .build();
                response.addCookie(deletedCookie);
            }
        } catch (JWTDecodeException e) {
            // 如果JWT令牌解析失败，记录错误日志并返回错误
            log.error("JWT令牌解析失败: {}", e.getMessage());
            return Mono.error(e);
        }

        // 返回空的Mono表示处理完成
        return Mono.empty();
    }
}
