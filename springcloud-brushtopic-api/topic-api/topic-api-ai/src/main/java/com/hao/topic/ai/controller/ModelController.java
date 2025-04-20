package com.hao.topic.ai.controller;

import com.hao.topic.ai.service.ModelService;
import com.hao.topic.model.dto.ai.ChatDto;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
    private final ModelService modelService;

    /**
     * 流式对话
     *
     * @param chatDto
     * @return
     */
    @PostMapping("/chat")
    public Flux<String> chat(@RequestBody ChatDto chatDto) {
        return modelService.chat(chatDto);

    }
}
