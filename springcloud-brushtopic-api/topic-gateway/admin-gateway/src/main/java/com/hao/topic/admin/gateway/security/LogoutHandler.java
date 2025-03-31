package com.hao.topic.admin.gateway.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.hao.topic.admin.gateway.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Slf4j
public class LogoutHandler implements ServerLogoutHandler {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Mono<Void> logout(WebFilterExchange webFilterExchange, Authentication authentication) {
        HttpCookie cookie = webFilterExchange.getExchange().getRequest().getCookies().getFirst("token");
        try {
            if (cookie != null) {
                Map<String, Object> userMap = JWTUtils.getTokenInfo(cookie.getValue());
                redisTemplate.delete((String) userMap.get("username"));
            }
        } catch (JWTDecodeException e) {
            return Mono.error(e);
        }

        return Mono.empty();
    }
}