package com.hao.topic.system.controller;

import com.hao.topic.client.security.SecurityFeignClient;
import com.hao.topic.common.result.Result;
import com.hao.topic.model.dto.system.SysUserListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Description: 用户控制器
 * Author: Hao
 * Date: 2025/4/10 21:43
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController {


    @Autowired
    private SecurityFeignClient securityFeignClient;


    /**
     * 获取用户列表
     *
     * @return
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> list(SysUserListDto sysUserListDto) {
        Map<String, Object> map = securityFeignClient.list(sysUserListDto);
        return Result.success(map);
    }
}
