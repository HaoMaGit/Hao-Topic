package com.hao.topic.model.vo.system;

import lombok.Data;

import java.util.List;

/**
 * Description: 菜单返回信息
 * Author: Hao
 * Date: 2025/4/5 21:49
 */
@Data
public class SysMenuVo {
    // 路由
    private String key;
    // 图标
    private String icon;
    // 菜单名称
    private String label;
    // 子菜单
    private List<SysMenuVo> children;
}
