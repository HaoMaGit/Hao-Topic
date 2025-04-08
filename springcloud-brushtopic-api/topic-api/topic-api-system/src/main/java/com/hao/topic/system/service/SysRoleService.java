package com.hao.topic.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hao.topic.model.entity.system.SysRole;

import java.util.Map;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/8 21:53
 */
public interface SysRoleService extends IService<SysRole> {
    Map<String, Object> roleList(SysRole sysRole);

    void add(SysRole sysRole);

    void update(SysRole sysRole);

    void delete(Long id);
}
