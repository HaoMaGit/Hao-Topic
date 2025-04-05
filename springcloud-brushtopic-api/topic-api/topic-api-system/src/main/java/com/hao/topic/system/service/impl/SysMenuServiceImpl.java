package com.hao.topic.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.model.entity.system.SysMenu;
import com.hao.topic.model.entity.system.SysRoleMenu;
import com.hao.topic.system.mapper.SysMenuMapper;
import com.hao.topic.system.mapper.SysRoleMenuMapper;
import com.hao.topic.system.service.SysMenuService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.awt.*;
import java.util.List;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/5 15:25
 */
@Service
@AllArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysMenuMapper sysMenuMapper;

    /**
     * 根据用户的角色id查询菜单权限
     *
     * @param roleId
     * @return
     */
    public List<SysMenu> getUserMenu(Long roleId) {
        if (roleId == null) {
            throw new TopicException(ResultCodeEnum.NO_ROLE_FAIL);
        }
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(sysRoleMenuLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(sysRoleMenus)) {
            throw new TopicException(ResultCodeEnum.NO_ROLE_FAIL);
        }
        // 提取到菜单id
        List<Long> menuIdList = sysRoleMenus.stream().map(SysRoleMenu::getMenuId).toList();
        if (CollectionUtils.isEmpty(menuIdList)) {
            throw new TopicException(ResultCodeEnum.NO_MENU_FAIL);
        }
        // 批量查询到菜单信息
        List<SysMenu> sysMenus = sysMenuMapper.selectBatchIds(menuIdList);
        // TODO 封装返回数据减少大小

        if (CollectionUtils.isEmpty(sysMenus)) {
            throw new TopicException(ResultCodeEnum.NO_MENU_FAIL);
        }
        // 返回
        return sysMenus;
    }
}
