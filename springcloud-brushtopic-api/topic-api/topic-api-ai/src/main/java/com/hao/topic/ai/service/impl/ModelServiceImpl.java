package com.hao.topic.ai.service.impl;

import com.hao.topic.ai.service.ModelService;
import com.hao.topic.model.dto.ai.ChatDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/20 14:45
 */
@Service
public class ModelServiceImpl implements ModelService {
    private final ChatClient chatClient;

    public ModelServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    /**
     * 使用api发起对话
     *
     * @param chatDto
     * @return
     */
    public Flux<String> chat(ChatDto chatDto) {
        // // 校验模式
        // if (chatDto.getModel().equals(AiConstant.SYSTEM_MODEL)) {
        //     // 系统模式
        //     return systemModel(chatDto);
        // } else if (chatDto.getModel().equals(AiConstant.AI_MODEL)) {
        //     // AI模式
        // } else {
        //     // 混合模式
        // }
        System.out.println(systemModel(chatDto));
        return null;
    }


    /**
     * 系统模式
     *
     * @param chatDto
     * @return
     */
    private String systemModel(ChatDto chatDto) {
        return this.chatClient.prompt()
                .user("你是谁")
                .call()
                .content();
    }

}
