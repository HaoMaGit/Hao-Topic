package com.hao.topic.client.system;

import com.hao.topic.common.auth.TokenInterceptor;
import com.hao.topic.model.entity.system.SysMenu;
import com.hao.topic.model.entity.system.SysRole;
import com.hao.topic.model.vo.system.SysMenuVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Description: 系统远程服务客户端 已被【认证服务】调用
 * Author: Hao
 * Date: 2025/4/5 15:47
 */
@FeignClient(name = "service-system", configuration = TokenInterceptor.class)
public interface SystemFeignClient {
    /**
     * 根据角色id查询菜单信息
     *
     * @param roleId
     * @return
     */
    @GetMapping("/system/menu/userMenu/{roleId}")
    public List<SysMenuVo> userMenu(@PathVariable Long roleId);

    /**
     * 根据角色i查询角色信息
     *
     * @param id
     * @return
     */
    @GetMapping("/system/role/{id}")
    public SysRole getById(@PathVariable Long id);

    /**
     * 根据角色权限key查询用户角色细腻系
     */
    @GetMapping("/system/role/key/{role}")
    public SysRole getByRoleKey(@PathVariable String role);
}
