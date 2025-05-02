package com.hao.topic.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/2 10:37
 */
@Data
public class LoginTypeDto {
    // 0账户登录 1邮箱登录
    @NotNull(message = "登录类型不能为空")
    private Integer loginType;
    private String account;
    @NotBlank(message = "登录密码不能为空")
    private String password;
    private String email;
}
