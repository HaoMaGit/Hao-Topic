package com.hao.topic.model.vo.system;

import com.hao.topic.model.entity.system.SysMenu;
import lombok.Data;

import java.util.List;

/**
 * Description: 用户信息返回
 * Author: Hao
 * Date: 2025/4/4 22:41
 */
@Data
public class UserInfoVo {
    private String account;
    private String avatar;
    private Integer identity;
    private Long id;
    // 菜单权限
    private List<SysMenuVo> menuList;
}
