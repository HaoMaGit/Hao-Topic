package com.hao.topic.model.vo.system;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: 菜单返回列表
 * Author: Hao
 * Date: 2025/4/6 22:02
 */
@Data
public class SysMenuListVo {
    private Long id;
    private String menuName;
    private Integer sorted;
    private String route;
    private String icon;
    private Long parentId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    // 子菜单
    private List<SysMenuListVo> children;
}
