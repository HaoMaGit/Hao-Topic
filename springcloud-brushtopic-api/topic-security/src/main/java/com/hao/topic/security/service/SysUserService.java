package com.hao.topic.security.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao.topic.client.system.SystemFeignClient;
import com.hao.topic.common.auth.TokenInterceptor;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.utils.JWTUtils;
import com.hao.topic.model.entity.system.SysMenu;
import com.hao.topic.model.entity.system.SysRole;
import com.hao.topic.model.entity.system.SysUser;
import com.hao.topic.model.entity.system.SysUserRole;
import com.hao.topic.model.vo.system.UserInfoVo;
import com.hao.topic.security.mapper.SysRoleMapper;
import com.hao.topic.security.mapper.SysUserMapper;
import com.hao.topic.security.mapper.SysUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Description: 系统用户接口层
 * Author: Hao
 * Date: 2025/4/1 10:54
 */
@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SystemFeignClient systemFeignClient;

    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    public SysUser findByUserName(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, username);
        return getOne(queryWrapper);
    }


    /**
     * 根据token获取用户信息
     *
     * @param token
     * @return
     */
    public UserInfoVo getUserInfo(String token) {
        if (token == null) {
            throw new TopicException(ResultCodeEnum.LOGIN_ERROR);
        }
        // 解析token
        Map<String, Object> tokenInfo = JWTUtils.getTokenInfo(token);
        // 校验
        if (CollectionUtils.isEmpty(tokenInfo)) {
            throw new TopicException(ResultCodeEnum.LOGIN_ERROR);
        }
        // 获取唯一用户名
        String username = (String) tokenInfo.get("username");
        // 校验
        if (username == null) {
            throw new TopicException(ResultCodeEnum.LOGIN_ERROR);
        }
        // 根据用户名获取用户信息
        SysUser sysUser = findByUserName(username);
        // 校验
        if (sysUser == null) {
            throw new TopicException(ResultCodeEnum.ACCOUNT_ERROR);
        }
        // 封装返回信息
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setAccount(sysUser.getAccount());
        userInfoVo.setAvatar(sysUser.getAvatar());

        // 根据用户id查询用户与角色的关系表
        SysUserRole sysUserRole = sysUserRoleMapper.selectOne(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, sysUser.getId()));
        // 校验
        if (sysUserRole == null) {
            throw new TopicException(ResultCodeEnum.ACCOUNT_ERROR);
        }
        // 根据角色id查询角色相关信息
        SysRole sysRole = sysRoleMapper.selectById(sysUserRole.getRoleId());
        // 校验一下
        if (sysRole == null) {
            throw new TopicException(ResultCodeEnum.ACCOUNT_ERROR);
        }
        userInfoVo.setIdentity(sysRole.getIdentify());
        // 设置token到ThreadLocal
        TokenInterceptor.setToken(token);
        // 调用系统管理服务 查询用户菜单权限
        List<SysMenu> sysMenus = systemFeignClient.userMenu(sysRole.getId());
        // 校验
        if (CollectionUtils.isEmpty(sysMenus)) {
            throw new TopicException(ResultCodeEnum.NO_MENU_FAIL);
        }

        userInfoVo.setMenuList(sysMenus);

        return userInfoVo;
    }
}
