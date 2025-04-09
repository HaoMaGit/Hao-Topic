package com.hao.topic.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao.topic.model.entity.system.SysRole;
import com.hao.topic.model.vo.system.SysMenuVo;
import com.hao.topic.system.mapper.SysRoleMapper;
import com.hao.topic.system.service.SysMenuService;
import com.hao.topic.system.service.SysRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/8 21:54
 */
@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    private final SysMenuService service;

    /**
     * 获取角色列表
     *
     * @param sysRole
     * @return
     */
    public Map<String, Object> roleList(SysRole sysRole) {
        // 设置分页参数
        Page<SysRole> sysRolePage = new Page<>(sysRole.getPageNum(), sysRole.getPageSize());
        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 名称校验
        if (sysRole.getName() != null) {
            sysRoleLambdaQueryWrapper.like(SysRole::getName, sysRole.getName());
        }
        // 开始分页查询
        Page<SysRole> sysRolePageResult = baseMapper.selectPage(sysRolePage, sysRoleLambdaQueryWrapper);

        // 封装返回结果
        return Map.of(
                "total", sysRolePageResult.getTotal(),
                "rows", sysRolePageResult.getRecords()
        );
    }

    /**
     * 添加角色
     *
     * @param sysRole
     */
    public void add(SysRole sysRole) {
        // baseMapper.insert(sysRole);
    }

    /**
     * 修改角色
     *
     * @param sysRole
     */
    public void update(SysRole sysRole) {
    }

    /**
     * 删除角色
     *
     * @param id
     */
    public void delete(Long id) {
    }

    /**
     * 获取角色下的菜单
     *
     * @param roleId
     * @return
     */
    public List<SysMenuVo> getRoleMenu(Long roleId) {
        // 校验id
        if (roleId == null) {
            return null;
        }
        try {
            // 查询该角色下的菜单
            return service.getUserMenu(roleId);
        } catch (Exception e) {
            return null;
        }
    }
}
