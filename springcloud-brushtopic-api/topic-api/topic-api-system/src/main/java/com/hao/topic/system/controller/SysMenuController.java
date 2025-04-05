package com.hao.topic.system.controller;

import com.hao.topic.model.entity.system.SysMenu;
import com.hao.topic.system.service.SysMenuService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
     * 根据角色获取用户菜单信息
     *
     * @param roleId
     * @return
     */
    @GetMapping("userMenu/{roleId}")
    public List<SysMenu> userMenu(@PathVariable Long roleId) {
        return sysMenuService.getUserMenu(roleId);
    }
}
