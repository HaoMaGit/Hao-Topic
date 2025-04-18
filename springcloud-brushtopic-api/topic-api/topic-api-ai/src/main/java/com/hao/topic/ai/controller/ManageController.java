package com.hao.topic.ai.controller;

import com.hao.topic.ai.service.ManageService;
import com.hao.topic.model.dto.ai.AiUserDto;
import com.hao.topic.model.dto.system.SysUserListDto;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Description: 用户ai管理
 * Author: Hao
 * Date: 2025/4/18 22:29
 */
@RestController
@RequestMapping("/ai/manage")
@AllArgsConstructor
public class ManageController {

    private final ManageService manageService;

    /**
     * 查询用户AI列表
     *
     * @param aiUserDto
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('admin')")
    public Map<String, Object> list(AiUserDto aiUserDto) {
        return manageService.list(aiUserDto);
    }
}
