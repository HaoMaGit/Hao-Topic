package com.haoma.topic.gateway.filter;


import com.alibaba.cloud.commons.lang.StringUtils;
import com.haoma.topic.gateway.utils.AppJwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;


/**
 * 网关全局过滤器
 */

@Component
public class AuthorizeFilter implements Ordered, GlobalFilter {

    /**
     * @param exchange 请求访问
     * @param chain    处理请求
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.获取request和response对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 定义需要放行的路径数组
        String[] allowedPaths = {"/api/user/login", "/api/user/captchaImage"};

        // 检查请求路径是否是放行路径之一
        boolean isAllowedPath = Arrays.stream(allowedPaths)
                .anyMatch(path -> request.getURI().getPath().equals(path));

        // 判断是否可以放行
        if (isAllowedPath) {
            // 放行
            return chain.filter(exchange);
        }


        // 3.获取token
        String token = request.getHeaders().getFirst("authorization");

        // 4.判断token是否存在
        if (StringUtils.isBlank(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        // 5.判断token是否有效
        try {
            // 获取payload信息
            Claims claimsBody = AppJwtUtil.getClaimsBody(token);
            // 是否是过期
            int result = AppJwtUtil.verifyToken(claimsBody);
            if (result == 1 || result == 2) {
                System.out.println("token过期了");
                // 过期了
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            // 6.解析token存入用户信息
            Object userId = claimsBody.get("id");
            // 向header中添加用户信息
            ServerHttpRequest serverHttpRequest = request.mutate().headers(
                    httpHeaders -> {
                        httpHeaders.add("userId", userId + "");
                    }
            ).build();
            // 重置请求头
            exchange.mutate().request(serverHttpRequest).build();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        // 6.放行
        return chain.filter(exchange);
    }

    /**
     * 优先级设置  值越小  优先级越高
     *
     * @return
     */
    @Override
    public int getOrder() {
        return -1;
    }
}
