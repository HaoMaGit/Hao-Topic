package com.hao.topic.client.security;

import com.hao.topic.common.auth.TokenInterceptor;
import com.hao.topic.common.result.Result;
import com.hao.topic.model.dto.system.SysUserDto;
import com.hao.topic.model.dto.system.SysUserListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Description: 授权认证服务客户端 已被【系统服务】调用
 * Author: Hao
 * Date: 2025/4/10 21:47
 */
@FeignClient(name = "topic-security", configuration = TokenInterceptor.class)
public interface SecurityFeignClient {

    @GetMapping("/security/user/list")
    public Map<String, Object> list(@SpringQueryMap SysUserListDto sysUserListDto);

    @PostMapping("/security/user/add")
    void add(@RequestBody SysUserDto sysUserDto);
}
