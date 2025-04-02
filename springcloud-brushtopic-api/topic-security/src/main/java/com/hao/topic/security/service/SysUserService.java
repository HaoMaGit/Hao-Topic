package com.hao.topic.security.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao.topic.model.entity.system.SysUser;
import com.hao.topic.security.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
