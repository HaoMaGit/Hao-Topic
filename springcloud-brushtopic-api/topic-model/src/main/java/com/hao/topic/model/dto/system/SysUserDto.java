package com.hao.topic.model.dto.system;

import com.hao.topic.model.entity.system.SysUser;
import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/12 15:18
 */

@Data
public class SysUserDto extends SysUser {
    private String roleName;
    private Long roleId;
}
