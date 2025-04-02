package com.hao.topic.security.filter;

import com.hao.topic.common.utils.JWTUtils;
import com.hao.topic.security.exception.NoFoundToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * 自定义Cookie到Header过滤器，用于将JWT令牌从Cookie中提取并添加到请求头中
 * <p>
 * 该类实现了 {@link WebFilter} 接口，用于在Spring Security中处理请求。
 * 当请求到达时，该过滤器会检查请求中的Cookie，如果找到名为 "token" 的Cookie，
 * 则将其值提取出来并添加到请求头的 "Authorization" 字段中，以便后续的认证和授权处理。
 *
 * @author hao
 */
@Slf4j
@Component
public class CookieToHeadersFilter implements WebFilter {

    /**
     * 处理请求，将JWT令牌从Cookie中提取并添加到请求头中
     * <p>
     * 当请求到达时，该方法会检查请求中的Cookie，如果找到名为 "token" 的Cookie，
     * 则将其值提取出来并添加到请求头的 "Authorization" 字段中，以便后续的认证和授权处理。
     *
     * @param exchange 当前的 {@link ServerWebExchange} 对象
     * @param chain    当前的 {@link WebFilterChain} 对象
     * @return {@link Mono<Void>} 表示处理完成
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        try {
            HttpCookie cookie = exchange.getRequest().getCookies().getFirst("token");
            if (cookie != null) {
                String token = cookie.getValue();
                if (token == null) {
                    log.error("Cookie的值为空");
                    throw new NoFoundToken("Cookie的值为空");
                }

                // 只负责将token添加到请求头
                exchange = exchange.mutate()
                        .request(exchange.getRequest().mutate()
                                .header(HttpHeaders.AUTHORIZATION, token)
                                .build())
                        .build();
            }
        } catch (NoFoundToken e) {
            log.error(e.getMsg());
        }
        return chain.filter(exchange);
    }

}
