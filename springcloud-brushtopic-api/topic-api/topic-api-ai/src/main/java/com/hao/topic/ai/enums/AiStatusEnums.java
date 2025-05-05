package com.hao.topic.ai.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/5 18:14
 */
@NoArgsConstructor
@AllArgsConstructor

public enum AiStatusEnums {
    // 发出面试题
    SEND_TOPIC(0, "发送面试题"),
    // 评估答案
    EVALUATE_ANSWER(1, "评估答案");

    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
