package com.hao.topic.security.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao.topic.client.system.SystemFeignClient;
import com.hao.topic.common.auth.TokenInterceptor;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.utils.JWTUtils;
import com.hao.topic.common.utils.StringUtils;
import com.hao.topic.model.dto.system.SysUserDto;
import com.hao.topic.model.dto.system.SysUserListDto;
import com.hao.topic.model.entity.system.SysRole;
import com.hao.topic.model.entity.system.SysUser;
import com.hao.topic.model.entity.system.SysUserRole;
import com.hao.topic.model.vo.system.SysMenuVo;
import com.hao.topic.model.vo.system.SysUserListVo;
import com.hao.topic.model.vo.system.UserInfoVo;
import com.hao.topic.security.mapper.SysUserMapper;
import com.hao.topic.security.mapper.SysUserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Description: 系统用户接口层
 * Author: Hao
 * Date: 2025/4/1 10:54
 */
@Service
@Slf4j
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {


    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SystemFeignClient systemFeignClient;

    @Autowired
    private SysUserMapper sysUserMapper;


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
        SysRole sysRole = systemFeignClient.getById(sysUserRole.getRoleId());
        // 校验一下
        if (sysRole == null) {
            throw new TopicException(ResultCodeEnum.ACCOUNT_ERROR);
        }
        userInfoVo.setIdentity(sysRole.getIdentify());
        // 设置token到ThreadLocal
        TokenInterceptor.setToken(token);
        // 调用系统管理服务 查询用户菜单权限
        List<SysMenuVo> sysMenus = systemFeignClient.userMenu(sysRole.getId());
        // 校验
        if (CollectionUtils.isEmpty(sysMenus)) {
            throw new TopicException(ResultCodeEnum.NO_MENU_FAIL);
        }
        userInfoVo.setMenuList(sysMenus);

        return userInfoVo;
    }

    /**
     * 获取用户列表
     *
     * @param sysUserListDto
     * @return
     */
    public Map<String, Object> userList(SysUserListDto sysUserListDto) {
        sysUserListDto.setPageNum(sysUserListDto.getPageNum() - 1);
        // 开始分页查询
        List<SysUserListVo> sysUserListVos = sysUserMapper.selectUserListVo(sysUserListDto);
        log.info("查询结果：{}", sysUserListVos);
        return Map.of(
                "total", sysUserListVos.size(),
                "rows", sysUserListVos
        );
    }

    /**
     * 添加用户
     *
     * @param sysUserDto
     */
    @Transactional
    public void add(SysUserDto sysUserDto) {
        // 校验账户不能为空
        if (StringUtils.isEmpty(sysUserDto.getAccount())) {
            throw new TopicException(ResultCodeEnum.PARAM_ACCOUNT_ERROR);
        }
        // 校验角色是否为空
        if (StringUtils.isEmpty(sysUserDto.getRoleName())) {
            throw new TopicException(ResultCodeEnum.PARAM_ROLE_ERROR);
        }
        // 密码不能为空
        if (StringUtils.isEmpty(sysUserDto.getPassword())) {
            throw new TopicException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        // 开始添加用户
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserDto, sysUser);
        // 密码加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(sysUser.getPassword());
        sysUser.setPassword(encode);
        // 添加用户
        sysUserMapper.insert(sysUser);
        // 添加用户角色关联表
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(sysUser.getId());
        sysUserRole.setRoleId(sysUserDto.getRoleId());
        sysUserRoleMapper.insert(sysUserRole);
    }

    /**
     * 修改用户
     *
     * @param sysUserDto
     */
    @Transactional
    public void update(SysUserDto sysUserDto) {
        // 校验账户不能为空
        if (StringUtils.isEmpty(sysUserDto.getAccount())) {
            throw new TopicException(ResultCodeEnum.PARAM_ACCOUNT_ERROR);
        }
        // 校验角色是否为空
        if (StringUtils.isEmpty(sysUserDto.getRoleName())) {
            throw new TopicException(ResultCodeEnum.PARAM_ROLE_ERROR);
        }
        // 修改用户
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserDto, sysUser);
        sysUserMapper.updateById(sysUser);
        // 删除角色关联表
        LambdaQueryWrapper<SysUserRole> sysUserRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserRoleLambdaQueryWrapper.eq(SysUserRole::getUserId, sysUserDto.getId());
        sysUserRoleMapper.delete(sysUserRoleLambdaQueryWrapper);
        // 修改用户角色关联表
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(sysUser.getId());
        sysUserRole.setRoleId(sysUserDto.getRoleId());
        sysUserRoleMapper.insert(sysUserRole);
    }

    /**
     * 删除用户
     *
     * @param ids
     */
    @Transactional
    public void delete(Long[] ids) {
        // 遍历
        for (Long id : ids) {
            // 删除用户
            sysUserMapper.deleteById(id);
            // 删除用户角色关联表
            LambdaQueryWrapper<SysUserRole> sysUserRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysUserRoleLambdaQueryWrapper.eq(SysUserRole::getUserId, id);
            sysUserRoleMapper.delete(sysUserRoleLambdaQueryWrapper);
        }
    }

    /**
     * 查询用户角色关系表
     *
     * @param roleId
     */
    public Boolean getByRoleId(Long roleId) {
        // 查询用户角色关联表
        LambdaQueryWrapper<SysUserRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.eq(SysUserRole::getRoleId, roleId);
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(sysRoleLambdaQueryWrapper);
        if (!CollectionUtils.isEmpty(sysUserRoles)) {
           return false;
        }
        return true;
    }
}
