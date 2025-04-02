package com.hao.topic.model.entity.system;

import com.hao.topic.model.entity.BaseEntity;
import lombok.Data;

/**
 * Description: 系统角色
 * Author: Hao
 * Date: 2025/4/1 10:12
 */
@Data
public class SysRole extends BaseEntity {
    private String name;
    private Integer identify;
    private String remark;
    private String roleKey;
}
