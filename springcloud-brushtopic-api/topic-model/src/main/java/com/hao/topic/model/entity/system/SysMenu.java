package com.hao.topic.model.entity.system;

import com.hao.topic.model.entity.BaseEntity;
import lombok.Data;

/**
 * Description: 系统菜单
 * Author: Hao
 * Date: 2025/4/1 10:10
 */
@Data
public class SysMenu extends BaseEntity {
    private String menuName;
    private Long parentId;
    private Integer sorted;
    private String route;
    private String icon;
}
