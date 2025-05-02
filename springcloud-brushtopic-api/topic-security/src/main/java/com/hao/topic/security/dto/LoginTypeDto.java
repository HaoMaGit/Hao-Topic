package com.hao.topic.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/2 10:37
 */
@Data
public class LoginTypeDto {
    // 0账户登录 1邮箱登录
    @NotBlank(message = "登录类型不能为空")
    private Integer loginType;
    @NotBlank(message = "登录账号不能为空")
    private String account;
    @NotBlank(message = "登录密码不能为空")
    private String password;
    @NotBlank(message = "邮箱不能为空")
    private String email;
}
