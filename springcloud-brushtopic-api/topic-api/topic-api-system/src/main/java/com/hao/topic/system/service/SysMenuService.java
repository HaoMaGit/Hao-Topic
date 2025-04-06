package com.hao.topic.system.service;

import com.hao.topic.model.entity.system.SysMenu;
import com.hao.topic.model.vo.system.SysMenuVo;

import java.util.List;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/5 15:24
 */
public interface SysMenuService {
    List<SysMenuVo> getUserMenu(Long roleId);
}
