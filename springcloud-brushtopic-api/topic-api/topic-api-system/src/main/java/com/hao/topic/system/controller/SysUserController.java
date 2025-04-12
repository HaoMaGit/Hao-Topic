package com.hao.topic.system.controller;

import com.alibaba.fastjson2.util.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hao.topic.api.utils.helper.MinioHelper;
import com.hao.topic.client.security.SecurityFeignClient;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.result.Result;
import com.hao.topic.common.utils.StringUtils;
import com.hao.topic.model.dto.system.SysUserDto;
import com.hao.topic.model.dto.system.SysUserListDto;
import com.hao.topic.model.entity.system.SysRole;
import com.hao.topic.model.vo.system.SysRoleVo;
import com.hao.topic.system.mapper.SysRoleMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description: 用户控制器
 * Author: Hao
 * Date: 2025/4/10 21:43
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController {


    @Autowired
    private SecurityFeignClient securityFeignClient;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private MinioHelper minioHelper;


    /**
     * 获取用户列表
     *
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('admin')")
    public Result<Map<String, Object>> list(SysUserListDto sysUserListDto) {
        Map<String, Object> map = securityFeignClient.list(sysUserListDto);
        return Result.success(map);
    }

    /**
     * 查询角色列表
     *
     * @return
     */
    @GetMapping("/roleList")
    @PreAuthorize("hasAuthority('admin')")
    public Result<List<SysRoleVo>> roleList() {
        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
        // 封装
        List<SysRoleVo> list = sysRoles.stream().map(sysRole -> {
            SysRoleVo sysRoleVo = new SysRoleVo();
            sysRoleVo.setRoleName(sysRole.getName());
            return sysRoleVo;
        }).toList();
        return Result.success(list);
    }

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/avatar")
    @PreAuthorize("hasAuthority('admin')")
    public Result upload(@RequestParam("avatar") MultipartFile file) {
        // 上传文件
        String url = minioHelper.uploadFile(file);
        return Result.success(url);
    }


    /**
     * 添加用户
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('admin')")
    public Result add(@RequestBody SysUserDto sysUserDto) {
        Boolean result = prepareSysUserDto(sysUserDto);
        if (!result) {
            return Result.fail(ResultCodeEnum.ROLE_NO_EXIST);
        }
        securityFeignClient.add(sysUserDto);
        return Result.success();
    }

    /**
     * 修改角色
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('admin')")
    public Result update(@RequestBody SysUserDto sysUserDto) {
        Boolean result = prepareSysUserDto(sysUserDto);
        if (!result) {
            return Result.fail(ResultCodeEnum.ROLE_NO_EXIST);
        }
        securityFeignClient.update(sysUserDto);
        return Result.success();
    }


    /**
     * 添加用户时准备数据校验角色
     *
     * @param sysUserDto
     * @return
     */
    private Boolean prepareSysUserDto(SysUserDto sysUserDto) {
        // 查询角色是否存在
        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.eq(SysRole::getName, sysUserDto.getRoleName());
        SysRole sysRole = sysRoleMapper.selectOne(sysRoleLambdaQueryWrapper);
        if (StringUtils.isNull(sysRole)) {
            return false;
        }
        // 如果是会员添加会员时间
        if (sysRole.getIdentify() == 1) {
            // 添加当前时间格式化
            LocalDateTime now = LocalDateTime.now();
            sysUserDto.setMemberTime(now);  // 直接设置 LocalDate
        }
        // 存在赋值
        sysUserDto.setRoleId(sysRole.getId());
        return true;
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/delete/{ids}")
    @PreAuthorize("hasAuthority('admin')")
    public Result delete(@PathVariable Long[] ids) {
        securityFeignClient.delete(ids);
        return Result.success();
    }
}
