package com.hao.topic.security.controller;

import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.result.Result;
import com.hao.topic.model.vo.system.UserInfoVo;
import com.hao.topic.security.dto.LoginRequestDto;
import com.hao.topic.security.handle.AuthenticationSuccessHandler;
import com.hao.topic.security.security.SecurityRepository;
import com.hao.topic.security.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.handler.DefaultWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/security/user")
@Slf4j
public class SecurityController {

    @Autowired
    private ReactiveAuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SysUserService sysUserService;


    /**
     * 登录接口
     *
     * @param exchange
     * @param loginRequest
     * @return
     */
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> login(ServerWebExchange exchange, @Validated @RequestBody LoginRequestDto loginRequest) {
        // 查询redis校验图片验证码
        String s = stringRedisTemplate.opsForValue().get(loginRequest.getCode());
        if (s == null || !s.equals(loginRequest.getCode())) {
            log.error("验证码错误");
            return Mono.error(new RuntimeException("验证码错误"));
        }

        exchange.getAttributes().put("remember", loginRequest.getRemember());

        return authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                ))
                .flatMap(authentication -> {
                    List<WebFilter> filters = new ArrayList<>();
                    DefaultWebFilterChain filterChain = new DefaultWebFilterChain(exchange1 -> Mono.empty(), filters);
                    WebFilterExchange webFilterExchange = new WebFilterExchange(exchange, filterChain);

                    // 直接返回认证成功处理器的结果
                    return authenticationSuccessHandler.onAuthenticationSuccess(webFilterExchange, authentication);
                })
                .onErrorResume(e -> {
                    log.error("认证失败", e);
                    return Mono.error(e);
                });
    }


    /**
     * 根据token获取用户信息
     *
     * @param token
     * @return
     */
    @GetMapping("/userInfo")
    public Result<UserInfoVo> getUserInfo(String token) {
        UserInfoVo userInfoVo = sysUserService.getUserInfo(token);
        return Result.success(userInfoVo);
    }
}