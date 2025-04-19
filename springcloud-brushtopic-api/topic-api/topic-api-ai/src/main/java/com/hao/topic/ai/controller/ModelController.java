package com.hao.topic.ai.controller;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Description: Ai模型控制器
 * Author: Hao
 * Date: 2025/4/18 22:30
 */
@RestController
@RequestMapping("ai/model")
@AllArgsConstructor
public class ModelController {
    private final ChatClient chatClient;

    /**
     * 流式对话
     *
     * @param prompt
     * @return
     */
    @GetMapping("/chat")
    public Flux<String> chat() {
        try {
            return chatClient
                    .prompt()
                    .user("你是谁？")
                    .stream()
                    .content();
        } catch (Exception e) {
            return Flux.just("出错了");
        }
    }
}
