package com.hao.topic.system.controller;

import com.hao.topic.client.security.SecurityFeignClient;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.result.Result;
import com.hao.topic.model.dto.system.SysRoleDto;
import com.hao.topic.model.entity.system.SysMenu;
import com.hao.topic.model.entity.system.SysRole;
import com.hao.topic.model.vo.system.SysMenuListVo;
import com.hao.topic.model.vo.system.SysMenuVo;
import com.hao.topic.system.mapper.SysRoleMapper;
import com.hao.topic.system.service.SysRoleService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Description: 用户控制器
 * Author: Hao
 * Date: 2025/4/8 21:52
 */
@RestController
@RequestMapping("/system/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;


    /**
     * 获取角色列表
     *
     * @param sysRole
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('admin')")
    public Result<Map<String, Object>> list(SysRole sysRole) {
        Map<String, Object> map = sysRoleService.roleList(sysRole);
        return Result.success(map);
    }

    /**
     * 添加角色
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('admin')")
    public Result add(@RequestBody @Validated SysRoleDto sysRoleDto) {
        sysRoleService.add(sysRoleDto);
        return Result.success();
    }

    /**
     * 修改角色
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('admin')")
    public Result update(@RequestBody @Validated SysRoleDto sysRoleDto) {
        sysRoleService.update(sysRoleDto);
        return Result.success();
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin')")
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

    /**
     * 获取角色下的菜单
     *
     * @param roleId
     * @return
     */
    @GetMapping("/menu/{roleId}")
    @PreAuthorize("hasAuthority('admin')")
    public Result<List<SysMenuVo>> getRoleMenu(@PathVariable Long roleId) {
        List<SysMenuVo> sysMenuVoList = sysRoleService.getRoleMenu(roleId);
        return Result.success(sysMenuVoList);
    }
}
