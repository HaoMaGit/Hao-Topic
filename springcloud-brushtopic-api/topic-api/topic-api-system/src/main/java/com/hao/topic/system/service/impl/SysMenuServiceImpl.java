package com.hao.topic.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.model.entity.system.SysMenu;
import com.hao.topic.model.entity.system.SysRoleMenu;
import com.hao.topic.model.vo.system.SysMenuListVo;
import com.hao.topic.model.vo.system.SysMenuVo;
import com.hao.topic.system.mapper.SysMenuMapper;
import com.hao.topic.system.mapper.SysRoleMenuMapper;
import com.hao.topic.system.service.SysMenuService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/5 15:25
 */
@Service
@AllArgsConstructor
@Slf4j
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysMenuMapper sysMenuMapper;

    /**
     * 根据用户的角色id查询菜单权限
     *
     * @param roleId
     * @return
     */
    public List<SysMenuVo> getUserMenu(Long roleId) {
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
        if (CollectionUtils.isEmpty(sysMenus)) {
            throw new TopicException(ResultCodeEnum.NO_MENU_FAIL);
        }
        // 创建返回菜单
        List<SysMenuVo> sysMenuVoList = new ArrayList<>();
        for (SysMenu sysMenu : sysMenus) {
            // 判断层级
            if (sysMenu.getParentId() == 0) {
                SysMenuVo sysMenuVo = new SysMenuVo();
                sysMenuVo.setKey(sysMenu.getRoute());
                sysMenuVo.setIcon(sysMenu.getIcon());
                sysMenuVo.setLabel(sysMenu.getMenuName());
                sysMenuVo.setId(sysMenu.getId());
                // 查询他的子集
                sysMenuVo.setChildren(getMenuChildren(sysMenu, sysMenus));
                sysMenuVoList.add(sysMenuVo);
            }
        }
        // 处理孤立的子菜单（没有父菜单的菜单）
        for (SysMenu sysMenuDb : sysMenus) {
            boolean hasParent = sysMenus.stream()
                    .anyMatch(menu -> menu.getId().equals(sysMenuDb.getParentId()));
            if (!hasParent && sysMenuDb.getParentId() != 0) {
                // 孤立的子菜单，直接作为根菜单处理
                SysMenuVo sysMenuVo = new SysMenuVo();
                sysMenuVo.setKey(sysMenuDb.getRoute());
                sysMenuVo.setIcon(sysMenuDb.getIcon());
                sysMenuVo.setLabel(sysMenuDb.getMenuName());
                sysMenuVo.setId(sysMenuDb.getId());
                sysMenuVoList.add(sysMenuVo);
            }
        }
        log.info("菜单信息：{}", sysMenuVoList.toArray());
        // 返回
        return sysMenuVoList;
    }

    /**
     * 查询菜单列表
     *
     * @param sysMenu
     * @return
     */
    public List<SysMenuListVo> menuList(SysMenu sysMenu) {
        // 封装条件
        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysMenuLambdaQueryWrapper.orderByAsc(SysMenu::getSorted);
        // 判断查询参数是否为空
        if (sysMenu.getMenuName() != null) {
            sysMenuLambdaQueryWrapper.like(SysMenu::getMenuName, sysMenu.getMenuName());
        }
        // 开始查询
        List<SysMenu> sysMenus = sysMenuMapper.selectList(sysMenuLambdaQueryWrapper);
        // 校验
        if (CollectionUtils.isEmpty(sysMenus)) {
            return new ArrayList<>();
        }
        // 封装返回结果
        List<SysMenuListVo> sysMenuListVos = new ArrayList<>();
        // 遍历
        for (SysMenu sysMenuDb : sysMenus) {
            if (sysMenuDb.getParentId() == 0) {
                // 是根菜单
                SysMenuListVo sysMenuVo = new SysMenuListVo();
                BeanUtils.copyProperties(sysMenuDb, sysMenuVo);
                // 如果有查询条件就查询子菜单
                if (sysMenu.getMenuName() != null) {
                    LambdaQueryWrapper<SysMenu> sysMenuLambdaQuery = new LambdaQueryWrapper<>();
                    sysMenuLambdaQuery.eq(SysMenu::getParentId, sysMenuDb.getId());
                    List<SysMenu> sysMenuChildren = sysMenuMapper.selectList(sysMenuLambdaQuery);
                    // 递归设置字菜单
                    sysMenuVo.setChildren(getMenuListChildren(sysMenuVo, sysMenuChildren));
                } else {
                    // 递归设置字菜单
                    sysMenuVo.setChildren(getMenuListChildren(sysMenuVo, sysMenus));
                }
                sysMenuListVos.add(sysMenuVo);
            }
        }
        // 处理孤立的子菜单（没有父菜单的菜单）
        for (SysMenu sysMenuDb : sysMenus) {
            boolean hasParent = sysMenus.stream()
                    .anyMatch(menu -> menu.getId().equals(sysMenuDb.getParentId()));
            if (!hasParent && sysMenuDb.getParentId() != 0) {
                // 孤立的子菜单，直接作为根菜单处理
                SysMenuListVo sysMenuVo = new SysMenuListVo();
                BeanUtils.copyProperties(sysMenuDb, sysMenuVo);
                sysMenuListVos.add(sysMenuVo);
            }
        }
        return sysMenuListVos;
    }

    /**
     * 添加菜单
     *
     * @param sysMenu
     */
    public void add(SysMenu sysMenu) {
        sysMenuMapper.insert(sysMenu);
    }

    /**
     * 修改菜单
     *
     * @param sysMenu
     */
    public void update(SysMenu sysMenu) {
        // 校验菜单id是否存在
        if (sysMenu.getId() == null) {
            throw new TopicException(ResultCodeEnum.MENU_ID_NOT_EXIST);
        }
        // 查询菜单是否存在
        SysMenu sysMenuDb = sysMenuMapper.selectById(sysMenu.getId());
        if (sysMenuDb == null) {
            throw new TopicException(ResultCodeEnum.MENU_ID_NOT_EXIST);
        }
        sysMenuMapper.updateById(sysMenu);
    }

    /**
     * 删除菜单
     *
     * @param id
     */
    public void delete(Long id) {
        // 查询这个菜单
        SysMenu sysMenu = sysMenuMapper.selectById(id);
        if (sysMenu == null) {
            throw new TopicException(ResultCodeEnum.MENU_ID_NOT_EXIST);
        }
        // 查询是否有子集
        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysMenuLambdaQueryWrapper.eq(SysMenu::getParentId, id);
        if (sysMenuMapper.selectCount(sysMenuLambdaQueryWrapper) > 0) {
            throw new TopicException(ResultCodeEnum.MENU_HAS_CHILDREN);
        }
        sysMenuMapper.deleteById(id);
    }

    /**
     * 查询子集列别菜单
     *
     * @param sysMenuVo
     * @param records
     * @return
     */
    private List<SysMenuListVo> getMenuListChildren(SysMenuListVo sysMenuVo, List<SysMenu> records) {
        List<SysMenuListVo> children = records.stream()
                .filter(menu -> menu.getParentId().equals(sysMenuVo.getId()))
                .sorted(Comparator.comparingInt(SysMenu::getSorted))
                .map(item -> {
                    SysMenuListVo sysMenuListVo = new SysMenuListVo();
                    BeanUtils.copyProperties(item, sysMenuListVo);
                    sysMenuListVo.setChildren(getMenuListChildren(sysMenuListVo, records));
                    return sysMenuListVo;
                }).toList();
        // 如果子菜单为空，设置为 null
        return children.isEmpty() ? null : children;
    }

    /**
     * 查询子集菜单
     *
     * @param sysMenu
     * @param sysMenus
     * @return
     */
    private List<SysMenuVo> getMenuChildren(SysMenu sysMenu, List<SysMenu> sysMenus) {
        // 遍历
        List<SysMenuVo> children = sysMenus.stream()
                .filter(menu1 -> menu1.getParentId().equals(sysMenu.getId()))
                .sorted(Comparator.comparingInt(SysMenu::getSorted)) // 子菜单排序
                .map(item -> {
                    SysMenuVo sysMenuVo = new SysMenuVo();
                    sysMenuVo.setKey(item.getRoute());
                    sysMenuVo.setIcon(item.getIcon());
                    sysMenuVo.setLabel(item.getMenuName());
                    sysMenuVo.setId(item.getId());
                    sysMenuVo.setChildren(getMenuChildren(item, sysMenus));
                    return sysMenuVo;
                }).toList();
        // 如果子菜单为空，设置为 null
        return children.isEmpty() ? null : children;
    }


}
