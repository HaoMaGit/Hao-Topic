package com.hao.topic.system.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Description: 通知类似枚举
 * Author: Hao
 * Date: 2025/5/3 16:31
 */
@AllArgsConstructor
@NoArgsConstructor
public enum NoticeEnums {
    MEMBER_PAY(0, "会员支付"),
    FEEDBACK(1, "意见反馈"),
    REPLY(2, "回复内容"),
    SUBJECT_FEEDBACK(3, "题目反馈");
    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    // 根据code值获取message
    public static String getMessageByCode(Integer code) {
        for (NoticeEnums noticeEnums : NoticeEnums.values()) {
            if (noticeEnums.getCode().equals(code)) {
                return noticeEnums.getMessage();
            }
        }
        return null;
    }
    }
