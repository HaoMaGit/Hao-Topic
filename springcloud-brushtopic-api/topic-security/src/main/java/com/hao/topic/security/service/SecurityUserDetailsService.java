package com.hao.topic.security.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hao.topic.client.system.SystemFeignClient;
import com.hao.topic.common.constant.ExceptionConstant;
import com.hao.topic.model.entity.system.SysRole;
import com.hao.topic.model.entity.system.SysUser;
import com.hao.topic.model.entity.system.SysUserRole;
import com.hao.topic.security.mapper.SysUserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
    private SystemFeignClient systemFeignClient;


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
        return Mono.fromCallable(() -> {
                    // 根据用户id获取用户角色信息
                    LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(SysUserRole::getUserId, sysUser.getId());
                    return sysUserRoleMapper.selectOne(wrapper);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(userRole -> {
                    if (userRole == null) {
                        return Mono.error(new UsernameNotFoundException(ExceptionConstant.USER_NOT_ROLE));
                    }
                    return Mono.fromCallable(() -> systemFeignClient.getById(userRole.getRoleId()))
                            .subscribeOn(Schedulers.boundedElastic());
                })
                .flatMap(sysRole -> {
                    if (sysRole == null) {
                        return Mono.error(new UsernameNotFoundException(ExceptionConstant.USER_NOT_ROLE));
                    }
                    if (sysRole.getIdentify() == 0) {
                        return Mono.error(new UsernameNotFoundException(ExceptionConstant.USER_NOT));
                    }

                    Collection<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(sysRole.getRoleKey()));

                    return Mono.just(new SecurityUserDetails(
                            sysUser.getAccount(),
                            "{bcrypt}" + sysUser.getPassword(),
                            authorities,
                            sysUser.getId()
                    ));
                });
    }
}