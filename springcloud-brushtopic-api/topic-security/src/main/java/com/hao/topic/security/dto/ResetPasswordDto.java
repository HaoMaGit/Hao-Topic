package com.hao.topic.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/2 19:29
 */
@Data
public class ResetPasswordDto {
    @NotBlank(message = "邮箱不能为空")
    private String email;
    @NotBlank(message = "验证码不能为空")
    private String code;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}
