package com.hao.topic.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Description: 登录请求参数
 * Author: Hao
 * Date: 2025/4/2 21:57
 */
@Data
public class LoginRequestDto {
    @NotBlank(message = "账户不能为空")
    private String username;
    @NotBlank(message = "账户密码不能为空")
    private String password;
    @NotBlank(message = "验证码不能为空")
    private String code;
    private Boolean remember;
}
