package com.hao.topic.model.entity.system;

import lombok.Data;

/**
 * Description: 角色菜单关联表
 * Author: Hao
 * Date: 2025/4/1 10:12
 */
@Data
public class SysRoleMenu {
    private Long roleId;
    private Long menuId;
}
