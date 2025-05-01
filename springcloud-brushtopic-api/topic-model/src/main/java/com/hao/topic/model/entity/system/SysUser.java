package com.hao.topic.model.entity.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hao.topic.model.entity.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: 系统用户
 * Author: Hao
 * Date: 2025/4/1 10:13
 */
@Data
public class SysUser extends BaseEntity {
    private String account;
    private String avatar;
    private String password;
    private String email;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime memberTime;
    private String nickname;
}
