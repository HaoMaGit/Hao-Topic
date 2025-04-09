package com.hao.topic.system.controller;

import com.hao.topic.common.result.Result;
import com.hao.topic.model.entity.system.SysMenu;
import com.hao.topic.model.vo.system.SysMenuListVo;
import com.hao.topic.model.vo.system.SysMenuVo;
import com.hao.topic.system.service.SysMenuService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Description: 菜单管理
 * Author: Hao
 * Date: 2025/4/5 15:24
 */
@RestController
@RequestMapping("/system/menu")
@AllArgsConstructor
public class SysMenuController {
    private final SysMenuService sysMenuService;


    /**
     * 查询菜单列表
     *
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('admin')")
    public Result<List<SysMenuListVo>> list(SysMenu sysMenu) {
        List<SysMenuListVo> sysMenuListVoList = sysMenuService.menuList(sysMenu);
        return Result.success(sysMenuListVoList);
    }

    /**
     * 添加菜单
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('admin')")
    public Result add(@RequestBody SysMenu sysMenu) {
        sysMenuService.add(sysMenu);
        return Result.success();
    }

    /**
     * 修改菜单
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('admin')")
    public Result update(@RequestBody SysMenu sysMenu) {
        sysMenuService.update(sysMenu);
        return Result.success();
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public Result delete(@PathVariable Long id) {
        sysMenuService.delete(id);
        return Result.success();
    }


    /**
     * 根据角色获取用户菜单信息
     *
     * @param roleId
     * @return
     */
    @GetMapping("userMenu/{roleId}")
    public List<SysMenuVo> userMenu(@PathVariable Long roleId) {
        return sysMenuService.getUserMenu(roleId);
    }


}
