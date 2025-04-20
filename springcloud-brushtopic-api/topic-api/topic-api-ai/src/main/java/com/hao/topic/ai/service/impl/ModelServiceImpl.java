package com.hao.topic.ai.service.impl;

import com.hao.topic.ai.service.ModelService;
import com.hao.topic.model.dto.ai.ChatDto;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/20 14:45
 */
@Service
public class ModelServiceImpl implements ModelService {
    @Autowired
    private OpenAIClient client;
    @Value("${spring.ai.openai.chat.options.model}")
    private String model;


    /**
     * /**
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
        // return chatClient
        //         .prompt()
        //         .user("你是谁？")
        //         .stream()
        //         .content();
    }


    /**
     * 系统模式
     *
     * @param chatDto
     * @return
     */
    private String systemModel(ChatDto chatDto) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage("你是谁")
                .model(model)
                .build();
        ChatCompletion chatCompletion = client.chat().completions().create(params);
        return chatCompletion.choices().get(0).message().content().orElse("无返回内容");
    }


}
