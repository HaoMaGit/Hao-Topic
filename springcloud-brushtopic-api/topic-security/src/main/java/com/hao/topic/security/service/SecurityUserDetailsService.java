package com.hao.topic.security.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hao.topic.common.constant.ExceptionConstant;
import com.hao.topic.common.result.Result;
import com.hao.topic.model.entity.system.SysRole;
import com.hao.topic.model.entity.system.SysUser;
import com.hao.topic.model.entity.system.SysUserRole;
import com.hao.topic.security.config.SecurityConfig;
import com.hao.topic.security.mapper.SysRoleMapper;
import com.hao.topic.security.mapper.SysUserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 自定义用户详细信息服务，用于从数据库中加载用户信息和权限
 * <p>
 * 该类实现了 {@link ReactiveUserDetailsService} 接口，用于在Spring Security中加载用户详细信息。
 * 它通过调用 {@link SysUserService} 从数据库中获取用户信息，并根据用户的角色设置相应的权限。
 *
 * @author your_name
 */
@Slf4j
@Service
public class SecurityUserDetailsService implements ReactiveUserDetailsService {


    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;


    /**
     * 1.需要验证权限都会经过这个方法
     * 根据用户名加载用户详细信息
     * <p>
     * 该方法根据提供的用户名从数据库中加载用户信息，并设置相应的角色权限。
     * 如果用户不存在，则抛出 {@link UsernameNotFoundException} 异常。
     *
     * @param account 用户名
     * @return 包含用户详细信息的 {@link Mono<UserDetails>}
     * @throws UsernameNotFoundException 如果用户不存在
     */
    @Override
    public Mono<UserDetails> findByUsername(String account) {
        log.info(account);
        // 调用数据库根据用户名获取用户
        SysUser sysUser = sysUserService.findByUserName(account);
        // TODO 添加用户需要加密处理
        // BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        // String encode = bCryptPasswordEncoder.encode(sysUser.getPassword());
        // System.out.println(encode);
        // 校验
        if (sysUser == null) {
            // 返回给前端
            return Mono.error(new UsernameNotFoundException(ExceptionConstant.USER_NOT_EXIST));
        }
        // 判断是否正常
        if (sysUser.getStatus() == 1) {
            return Mono.error(new UsernameNotFoundException(ExceptionConstant.USER_NOT_STOP));
        }
        log.info(sysUser.toString());
        // 根据用户id获取用户角色信息
        LambdaQueryWrapper<SysUserRole> sysUserRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserRoleLambdaQueryWrapper.eq(SysUserRole::getUserId, sysUser.getId());
        SysUserRole sysUserRole = sysUserRoleMapper.selectOne(sysUserRoleLambdaQueryWrapper);
        // 校验
        if (sysUserRole == null) {
            return Mono.error(new UsernameNotFoundException(ExceptionConstant.USER_NOT_ROLE));
        }
        // 根据角色id获取用户角色信息
        SysRole sysRole = sysRoleMapper.selectById(sysUserRole.getRoleId());
        // 校验
        if (sysRole == null) {
            return Mono.error(new UsernameNotFoundException(ExceptionConstant.USER_NOT_ROLE));
        }
        // user用户不能访问
        if (sysRole.getIdentify() == 0) {
            return Mono.error(new UsernameNotFoundException(ExceptionConstant.USER_NOT));
        }
        log.info(sysRole.toString());
        // 存储权限
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(sysRole.getRoleKey()));
        // 创建 SecurityUserDetails 对象时，对密码进行编码
        SecurityUserDetails securityUserDetails = new SecurityUserDetails(
                sysUser.getAccount(),
                "{bcrypt}" + sysUser.getPassword(),  // 加密
                authorities,
                sysUser.getId()
        );
        return Mono.just(securityUserDetails);
    }
}