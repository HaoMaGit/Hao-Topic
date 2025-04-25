package com.hao.topic.client.security;

import com.hao.topic.common.auth.TokenInterceptor;
import com.hao.topic.model.dto.ai.AiUserDto;
import com.hao.topic.model.dto.system.SysUserDto;
import com.hao.topic.model.dto.system.SysUserListDto;
import com.hao.topic.model.excel.sytem.SysUserExcel;
import com.hao.topic.model.excel.sytem.SysUserExcelExport;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Description: 授权认证服务客户端 已被【系统服务】调用
 * Author: Hao
 * Date: 2025/4/10 21:47
 */
@FeignClient(name = "topic-security", configuration = TokenInterceptor.class)
public interface SecurityFeignClient {

    /**
     * 获取用户列表
     *
     * @param sysUserListDto
     * @return
     */
    @GetMapping("/security/user/list")
    Map<String, Object> list(@SpringQueryMap SysUserListDto sysUserListDto);

    /**
     * 新增用户
     *
     * @param sysUserDto
     */
    @PostMapping("/security/user/add")
    void add(@RequestBody SysUserDto sysUserDto);

    /**
     * 修改用户
     *
     * @param sysUserDto
     */
    @PostMapping("/security/user/update")
    void update(@RequestBody SysUserDto sysUserDto);

    /**
     * 删除用户
     *
     * @param ids
     */
    @DeleteMapping("/security/user/delete/{ids}")
    void delete(@PathVariable Long[] ids);

    /**
     * 根据角色id查询用户
     *
     * @param roleId
     * @return
     */
    @GetMapping("/security/user/getByRoleId/{roleId}")
    Boolean getByRoleId(@PathVariable Long roleId);

    /**
     * 获取excelVo数据
     *
     * @param sysUserListDto
     * @param ids
     * @return
     */
    @GetMapping("/security/user/export/{ids}")
    List<SysUserExcelExport> getExcelVo(SysUserListDto sysUserListDto, @PathVariable Long[] ids);

    /**
     * 将excel数据插入到数据库
     *
     * @param excelVoList
     * @param updateSupport
     */
    @PostMapping("/security/user/import")
    String importExcel(@RequestBody List<SysUserExcel> excelVoList, @RequestParam("updateSupport") Boolean updateSupport);


}
