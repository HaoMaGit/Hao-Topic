package com.hao.topic.admin.gateway.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 自定义授权管理器，用于检查用户是否有权限访问特定路径
 * <p>
 * 该类实现了ReactiveAuthorizationManager接口，用于在Spring Security中进行自定义的授权逻辑。
 * 它根据用户的权限和请求路径来决定是否允许用户访问该路径。
 *
 * @author your_name
 */
@Slf4j
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    /**
     * 检查用户是否有权限访问特定路径
     * <p>
     * 该方法根据用户的权限和请求路径来决定是否允许用户访问该路径。
     * 如果用户具有相应的权限，则返回AuthorizationDecision(true)；
     * 否则返回AuthorizationDecision(false)。
     *
     * @param authentication       包含当前认证信息的Mono
     * @param authorizationContext 包含当前请求的AuthorizationContext
     * @return Mono<AuthorizationDecision> 表示授权决策结果
     */
    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        return authentication.map(auth -> {
            // 获取请求路径
            String path = authorizationContext.getExchange().getRequest().getURI().getPath();
            log.info("Checking access for path: {}", path);

            // 遍历用户的权限
            for (GrantedAuthority authority : auth.getAuthorities()) {
                log.info("User has authority: {}", authority.getAuthority());

                // 检查用户是否有ROLE_USER权限并且路径包含/user/normal
                if (authority.getAuthority().equals("ROLE_USER") && path.contains("/user/normal")) {
                    log.info("User has ROLE_USER and path contains /user/normal, granting access.");
                    return new AuthorizationDecision(true);
                }
                // 检查用户是否有ROLE_ADMIN权限并且路径包含/api/user/admin
                else if (authority.getAuthority().equals("ROLE_ADMIN") && path.contains("/api/user/admin")) {
                    log.info("User has ROLE_ADMIN and path contains /api/user/admin, granting access.");
                    return new AuthorizationDecision(true);
                }
            }

            // 如果没有匹配的权限，拒绝访问
            log.info("No matching authority found for path: {}", path);
            return new AuthorizationDecision(false);
        }).defaultIfEmpty(new AuthorizationDecision(false));
    }
}
