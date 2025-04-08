package com.hao.topic.system.controller;

import com.hao.topic.common.result.Result;
import com.hao.topic.model.entity.system.SysMenu;
import com.hao.topic.model.entity.system.SysRole;
import com.hao.topic.system.mapper.SysRoleMapper;
import com.hao.topic.system.service.SysRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Description: 用户控制器
 * Author: Hao
 * Date: 2025/4/8 21:52
 */
@RestController
@RequestMapping("/system/role")
@AllArgsConstructor
public class SysRoleController {

    private final SysRoleService sysRoleService;

    /**
     * 获取角色列表
     *
     * @param sysRole
     * @return
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> list(SysRole sysRole) {
        Map<String, Object> map = sysRoleService.roleList(sysRole);
        return Result.success(map);
    }

    /**
     * 添加角色
     */
    @PostMapping("/add")
    public Result add(@RequestBody SysRole sysRole) {
        sysRoleService.add(sysRole);
        return Result.success();
    }

    /**
     * 修改角色
     */
    @PutMapping("/update")
    public Result update(@RequestBody SysRole sysRole) {
        sysRoleService.update(sysRole);
        return Result.success();
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        sysRoleService.delete(id);
        return Result.success();
    }


    /**
     * 根据角色信息获取用户角色信息
     */
    @GetMapping("/{id}")
    public SysRole getById(@PathVariable Long id) {
        return sysRoleService.getById(id);
    }
}
