package com.hao.topic.client.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hao.topic.common.auth.TokenInterceptor;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.result.Result;
import com.hao.topic.model.dto.system.SysUserDto;
import com.hao.topic.model.dto.system.SysUserListDto;
import com.hao.topic.model.entity.system.SysUserRole;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Description: 授权认证服务客户端 已被【系统服务】调用
 * Author: Hao
 * Date: 2025/4/10 21:47
 */
@FeignClient(name = "topic-security", configuration = TokenInterceptor.class)
public interface SecurityFeignClient {

    @GetMapping("/security/user/list")
    Map<String, Object> list(@SpringQueryMap SysUserListDto sysUserListDto);

    @PostMapping("/security/user/add")
    void add(@RequestBody SysUserDto sysUserDto);

    @PostMapping("/security/user/update")
    void update(@RequestBody SysUserDto sysUserDto);

    @DeleteMapping("/security/user/delete/{ids}")
    void delete(@PathVariable Long[] ids);

    @GetMapping("/security/user/getByRoleId/{roleId}")
    Boolean getByRoleId(@PathVariable Long roleId);
}
