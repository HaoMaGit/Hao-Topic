package com.hao.topic.model.dto.system;

import com.hao.topic.model.entity.system.SysRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

/**
 * Description: 角色请求数据
 * Author: Hao
 * Date: 2025/4/9 22:40
 */
@Data
public class SysRoleDto {
    // 菜单ids
    private List<Long> menuIds;
    @NotBlank(message = "角色名称不能为空")
    private String name;
    @NotNull(message = "角色标识不能为空")
    private Integer identify;
    @NotBlank(message = "角色描述不能为空")
    private String remark;
    @NotBlank(message = "角色标识不能为空")
    private String roleKey;
    private Long id;
}
