package com.hao.topic.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao.topic.client.security.SecurityFeignClient;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.model.dto.system.SysRoleDto;
import com.hao.topic.model.entity.system.SysRole;
import com.hao.topic.model.entity.system.SysRoleMenu;
import com.hao.topic.model.entity.system.SysUserRole;
import com.hao.topic.model.vo.system.SysMenuVo;
import com.hao.topic.security.mapper.SysUserRoleMapper;
import com.hao.topic.system.mapper.SysRoleMapper;
import com.hao.topic.system.mapper.SysRoleMenuMapper;
import com.hao.topic.system.service.SysMenuService;
import com.hao.topic.system.service.SysRoleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SecurityFeignClient securityFeignClient;
    private final SysRoleMapper sysRoleMapper;

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
     * @param sysRoleDto
     */
    public void add(SysRoleDto sysRoleDto) {
        // 拷贝
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleDto, sysRole);
        baseMapper.insert(sysRole);
        // 校验添加角色菜单关联
        if (sysRoleDto.getMenuIds() != null && sysRoleDto.getMenuIds().size() > 0) {
            // 封装角色菜单数据
            List<SysRoleMenu> list = sysRoleDto.getMenuIds().stream().map(id -> {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(sysRole.getId());
                sysRoleMenu.setMenuId(id);
                return sysRoleMenu;
            }).toList();
            // 添加角色菜单关联
            for (SysRoleMenu sysRoleMenu : list) {
                sysRoleMenuMapper.insert(sysRoleMenu);
            }
        }
    }

    /**
     * 修改角色
     *
     * @param sysRole
     */
    public void update(SysRoleDto sysRole) {
        SysRole sysRoleDb = baseMapper.selectById(sysRole.getId());
        if (sysRoleDb == null) {
            throw new TopicException(ResultCodeEnum.ROLE_NOT_EXIST);
        }
        BeanUtils.copyProperties(sysRole, sysRoleDb);
        // 修改
        baseMapper.updateById(sysRoleDb);
        // 删除菜单角色关系
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getRoleId, sysRole.getId());
        sysRoleMenuMapper.delete(sysRoleMenuLambdaQueryWrapper);
        // 判断菜单是否发生变化
        if (!CollectionUtils.isEmpty(sysRole.getMenuIds())) {
            // 添加
            for (Long menuId : sysRole.getMenuIds()) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(sysRole.getId());
                sysRoleMenu.setMenuId(menuId);
                sysRoleMenuMapper.insert(sysRoleMenu);
            }
        }
    }

    /**
     * 删除角色
     *
     * @param roleId
     */
    public void delete(Long roleId) {
        // 校验
        if (roleId == null) {
            throw new TopicException(ResultCodeEnum.DEL_ROLE_ERROR);
        }
        // 远程调用
        Boolean byRoleId = securityFeignClient.getByRoleId(roleId);
        if (!byRoleId) {
            throw new TopicException(ResultCodeEnum.ROLE_USER_ERROR);
        }
        // 删除角色菜单关系表
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        sysRoleMenuMapper.delete(sysRoleMenuLambdaQueryWrapper);
        // 删除角色
        baseMapper.deleteById(roleId);
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

    /**
     * 根据角色key获取角色信息
     *
     * @param roleKey
     * @return
     */
    public SysRole getRoleKey(String roleKey) {
        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.eq(SysRole::getRoleKey, roleKey);
        SysRole sysRole = sysRoleMapper.selectOne(sysRoleLambdaQueryWrapper);
        if (sysRole == null) {
            return null;
        }
        return sysRole;
    }
}
