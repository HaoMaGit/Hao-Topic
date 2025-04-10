package com.hao.topic.model.vo.system;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: 用户列表返回数据
 * Author: Hao
 * Date: 2025/4/10 22:10
 */
@Data
public class SysUserListVo {
    private Long id;
    private String account;
    private String avatar;
    private String email;
    private Integer status;
    private String roleName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime memberTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
