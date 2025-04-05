package com.hao.topic.security.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.utils.JWTUtils;
import com.hao.topic.model.entity.system.SysUser;
import com.hao.topic.model.vo.system.UserInfoVo;
import com.hao.topic.security.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Description: 系统用户接口层
 * Author: Hao
 * Date: 2025/4/1 10:54
 */
@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {
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
        // TODO 查询用户菜单权限
        userInfoVo.setMenuList(null);

        return userInfoVo;
    }
}
