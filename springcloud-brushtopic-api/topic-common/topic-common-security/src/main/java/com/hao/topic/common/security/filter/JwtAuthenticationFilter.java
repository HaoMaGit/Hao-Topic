package com.hao.topic.common.security.filter;

import com.hao.topic.common.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * 自定义的JWT认证过滤器，用于处理JWT令牌的验证和权限设置。
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * 注入RedisTemplate，用于与Redis进行交互。
     */
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 重写doFilterInternal方法，实现JWT令牌的验证和权限设置。
     *
     * @param request     HTTP请求对象
     * @param response    HTTP响应对象
     * @param filterChain 过滤器链对象
     * @throws ServletException 如果发生Servlet异常
     * @throws IOException      如果发生I/O异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 从请求头中获取Authorization字段的值，即JWT令牌
        String token = request.getHeader("Authorization");
        log.info("获取到的令牌: {}", token);
        // 检查令牌是否存在
        if (token != null) {
            try {
                // 使用JWTUtils解析JWT令牌，获取用户信息
                Map<String, Object> userMap = JWTUtils.getTokenInfo(token);
                String username = (String) userMap.get("username");

                // 从Redis中获取存储的令牌，验证令牌是否有效
                String result = (String) redisTemplate.opsForValue().get(username);
                if (result == null || !result.equals(token)) {
                    log.info("令牌无效或未找到");
                } else {
                    // 根据令牌中的角色信息创建权限集合
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    String role = (String) userMap.get("role");
                    log.info("用户角色: {}", role);

                    // 添加两种格式的权限，确保能匹配 @PreAuthorize 中的表达式
                    authorities.add(new SimpleGrantedAuthority(role));
                    // 创建认证对象并设置到安全上下文
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // 捕获并记录加载安全上下文时发生的异常
                log.error("加载安全上下文时发生异常: {}", e.getMessage());
                SecurityContextHolder.clearContext();  // 清除安全上下文
            }
        } else {
            log.info("未找到令牌");
        }
        // 继续执行过滤器链中的下一个过滤器
        filterChain.doFilter(request, response);
    }
}
