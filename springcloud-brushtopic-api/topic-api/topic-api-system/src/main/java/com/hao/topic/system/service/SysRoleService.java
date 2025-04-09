package com.hao.topic.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hao.topic.model.dto.system.SysRoleDto;
import com.hao.topic.model.entity.system.SysRole;
import com.hao.topic.model.vo.system.SysMenuVo;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/8 21:53
 */
public interface SysRoleService extends IService<SysRole> {
    Map<String, Object> roleList(SysRole sysRole);

    void add(SysRoleDto sysRoleDto);

    void update(SysRoleDto sysRole);

    void delete(Long id);

    List<SysMenuVo> getRoleMenu(Long roleId);

}
