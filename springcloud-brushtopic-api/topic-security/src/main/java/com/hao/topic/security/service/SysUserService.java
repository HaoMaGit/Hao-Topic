package com.hao.topic.security.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
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
import com.hao.topic.model.excel.sytem.SysUserExcel;
import com.hao.topic.model.excel.sytem.SysUserExcelExport;
import com.hao.topic.model.vo.system.SysMenuVo;
import com.hao.topic.model.vo.system.SysUserListVo;
import com.hao.topic.model.vo.system.UserInfoVo;
import com.hao.topic.security.dto.UserDto;
import com.hao.topic.security.mapper.SysUserMapper;
import com.hao.topic.security.mapper.SysUserRoleMapper;
import com.hao.topic.security.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
        userInfoVo.setId(sysUser.getId());

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
        if (sysUserListDto.getPageNum() != null) {
            sysUserListDto.setPageNum((sysUserListDto.getPageNum() - 1) * sysUserListDto.getPageSize());
        }
        // 开始分页查询
        List<SysUserListVo> sysUserListVos = sysUserMapper.selectUserListVo(sysUserListDto);
        // 查询总记录数
        int total = sysUserMapper.countUserList(sysUserListDto);
        log.info("查询结果：{}", sysUserListVos);
        return Map.of(
                "total", total,
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
        /// 查询这个用户是否存在
        if (findByUserName(sysUser.getAccount()) != null) {
            throw new TopicException(ResultCodeEnum.ADD_USER_ERROR);
        }
        // 添加用户
        int insert = sysUserMapper.insert(sysUser);
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

    /**
     * 获取excelVo数据
     *
     * @param sysUserListDto
     * @param ids
     * @return
     */
    public List<SysUserExcelExport> getExcelVo(SysUserListDto sysUserListDto, Long[] ids) {
        if (ids[0] != 0) {
            // 查询用户
            List<SysUser> sysUsers = sysUserMapper.selectBatchIds(Arrays.asList(ids));
            if (!CollectionUtils.isEmpty(sysUsers)) {
                // 遍历
                List<SysUserExcelExport> sysUserExcelExports = new ArrayList<>();
                for (SysUser sysUser : sysUsers) {
                    SysUserExcelExport sysUserExcelExport = new SysUserExcelExport();
                    BeanUtils.copyProperties(sysUser, sysUserExcelExport);
                    // 根据用户id查询用户角色信息
                    LambdaQueryWrapper<SysUserRole> sysUserRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    sysUserRoleLambdaQueryWrapper.eq(SysUserRole::getUserId, sysUser.getId());
                    SysUserRole sysUserRole = sysUserRoleMapper.selectOne(sysUserRoleLambdaQueryWrapper);
                    if (sysUserRole != null) {
                        SysRole byId = systemFeignClient.getById(sysUserRole.getRoleId());
                        if (byId != null) {
                            sysUserExcelExport.setRoleName(byId.getName());
                        }
                    }
                    sysUserExcelExports.add(sysUserExcelExport);
                }
                // 倒序
                sysUserExcelExports.sort(Comparator.comparing(SysUserExcelExport::getId).reversed());
                return sysUserExcelExports;
            }
        }
        // 没有选择
        // 开始分页查询
        List<SysUserListVo> sysUserListVos = sysUserMapper.selectUserListVo(sysUserListDto);
        // 封装
        if (!CollectionUtils.isEmpty(sysUserListVos)) {
            // 遍历
            List<SysUserExcelExport> sysUserExcelExports = new ArrayList<>();
            for (SysUserListVo sysUserListVo : sysUserListVos) {
                SysUserExcelExport sysUserExcelExport = new SysUserExcelExport();
                BeanUtils.copyProperties(sysUserListVo, sysUserExcelExport);
                sysUserExcelExports.add(sysUserExcelExport);
            }
            return sysUserExcelExports;
        }
        return null;
    }

    /**
     * 将excel数据插入到数据库
     *
     * @param excelVoList
     * @param updateSupport
     */
    @Transactional
    public String importExcel(List<SysUserExcel> excelVoList, Boolean updateSupport) {
        // 校验
        if (StringUtils.isNull(excelVoList) || excelVoList.size() == 0) {
            throw new TopicException(ResultCodeEnum.IMPORT_EXCEL_ERROR);
        }
        // 计算成功的数量
        int successNum = 0;
        // 计算失败的数量
        int failureNum = 0;
        // 拼接成功消息
        StringBuilder successMsg = new StringBuilder();
        // 拼接错误消息
        StringBuilder failureMsg = new StringBuilder();
        // 遍历
        for (SysUserExcel sysUserExcelTemplate : excelVoList) {
            try {
                // 根据名称查询当用户是否存在
                SysUser sysUser = findByUserName(sysUserExcelTemplate.getAccount());
                if (StringUtils.isNull(sysUser)) {
                    // 不存在插入
                    // 处理密码
                    sysUserExcelTemplate.setPassword(PasswordUtils.encodePassword(sysUserExcelTemplate.getPassword()));
                    // 根据角色名称判断是否存在
                    SysRole sysRole = sysUserMapper.getByRoleName(sysUserExcelTemplate.getRoleName());
                    if (StringUtils.isNull(sysRole)) {
                        // 不存在
                        failureNum++;
                        failureMsg.append("<br/>").append(failureNum).append("-用户：").append(sysUserExcelTemplate.getAccount()).append("-角色不存在");
                    } else {
                        // 存在
                        // 添加
                        SysUser user = new SysUser();
                        BeanUtils.copyProperties(sysUserExcelTemplate, user);
                        sysUserMapper.insert(user);
                        // 插入角色到关系表中
                        SysUserRole sysUserRole = new SysUserRole();
                        sysUserRole.setUserId(user.getId());
                        sysUserRole.setRoleId(sysRole.getId());
                        sysUserRoleMapper.insert(sysUserRole);
                        successNum++;
                        successMsg.append("<br/>").append(successNum).append("-用户：").append(sysUserExcelTemplate.getAccount()).append("-导入成功");
                    }
                } else if (updateSupport) {
                    // 更新
                    // 处理密码
                    sysUserExcelTemplate.setPassword(PasswordUtils.encodePassword(sysUserExcelTemplate.getPassword()));
                    // 根据角色名称判断是否存在
                    SysRole sysRole = sysUserMapper.getByRoleName(sysUserExcelTemplate.getRoleName());
                    if (StringUtils.isNull(sysRole)) {
                        // 不存在
                        failureNum++;
                        failureMsg.append("<br/>").append(failureNum).append("-用户：").append(sysUserExcelTemplate.getAccount()).append("-角色不存在");
                    } else {
                        // 修改
                        BeanUtils.copyProperties(sysUserExcelTemplate, sysUser);
                        sysUserMapper.updateById(sysUser);
                        // 删除角色关联表
                        LambdaQueryWrapper<SysUserRole> sysUserRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        sysUserRoleLambdaQueryWrapper.eq(SysUserRole::getUserId, sysUser.getId());
                        sysUserRoleMapper.delete(sysUserRoleLambdaQueryWrapper);
                        // 插入角色到关系表中
                        SysUserRole sysUserRole = new SysUserRole();
                        sysUserRole.setUserId(sysUser.getId());
                        sysUserRole.setRoleId(sysRole.getId());
                        sysUserRoleMapper.insert(sysUserRole);
                        successNum++;
                        successMsg.append("<br/>").append(successNum).append("-用户：").append(sysUserExcelTemplate.getAccount()).append("-更新成功");
                    }
                } else {
                    // 已存在
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("-用户：").append(sysUser.getAccount()).append("-已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "-用户： " + sysUserExcelTemplate.getAccount() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new TopicException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }


    /**
     * 保存用户头像
     *
     * @param userDto
     */
    public void saveAvatar(UserDto userDto) {
        if (userDto.getId() == null) {
            throw new TopicException(ResultCodeEnum.USER_NOT_EXIST);
        }
        SysUser sysUserDb = sysUserMapper.selectById(userDto.getId());
        if (sysUserDb == null) {
            throw new TopicException(ResultCodeEnum.USER_NOT_EXIST);
        }
        sysUserDb.setAvatar(userDto.getAvatar());
        sysUserMapper.updateById(sysUserDb);
    }

    /**
     * 修改账号昵称和邮箱
     *
     * @param userDto
     */
    public void updateNicknameAndEmail(UserDto userDto) {

    }
}
