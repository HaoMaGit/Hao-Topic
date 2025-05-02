package com.hao.topic.security.constant;

import lombok.Getter;

/**
 * Description: 发送邮箱验证码常量
 * Author: Hao
 * Date: 2025/5/2 18:21
 */
@Getter
public enum EmailConstant {
    // 邮件发送成功提示
    EMAIL_SEND_SUCCESS("验证码已发送，请注意查收邮件"),

    // 邮件正文前缀
    EMAIL_MESSAGE("您的 HaoAi刷题 平台验证码为："),

    // 验证码过期时间说明
    EMAIL_OUT_TIME("，验证码有效期为2分钟，请及时填写。"),

    // 邮件标题
    EMAIL_TITLE("HaoAi刷题 邮箱验证码"),

    // Redis中验证码的key（建议加前缀避免冲突）
    EMAIL_CODE("haoai:email:code:"),

    // 验证码错误提示
    EMAIL_CODE_ERROR("验证码错误，请重新输入正确的验证码。"),

    // 邮箱不存在提示
    NOT_EXIST_EMAIL("该邮箱未注册，请确认邮箱是否正确");

    private final String Value;

    EmailConstant(String value) {
        this.Value = value;
    }
}