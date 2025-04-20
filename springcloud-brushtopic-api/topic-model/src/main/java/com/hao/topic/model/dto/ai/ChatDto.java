package com.hao.topic.model.dto.ai;

import lombok.Data;

/**
 * Description: 请求ai数据
 * Author: Hao
 * Date: 2025/4/20 14:40
 */
@Data
public class ChatDto {
    // 消息
    private String prompt;
    // 对话id
    private Long chatId;
    // 当前对话id
    private Long currentChatId;
    // 模式
    private String model;
}
