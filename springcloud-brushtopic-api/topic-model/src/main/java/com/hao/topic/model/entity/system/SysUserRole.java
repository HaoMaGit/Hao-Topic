package com.hao.topic.model.entity.system;

import com.hao.topic.model.entity.BaseEntity;
import lombok.Data;

/**
 * Description: 用户角色关联表
 * Author: Hao
 * Date: 2025/4/1 10:14
 */
@Data
public class SysUserRole extends BaseEntity {
    private Long userId;
    private Long roleId;
}
