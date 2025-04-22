package com.hao.topic.ai.config;

import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesisParam;
import com.hao.topic.ai.properties.TtsProperties;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ai相关配置
 */
@Configuration
public class ChatConfiguration {

    @Bean
    public ChatClient chatClient(OpenAiChatModel openAiChatModel) {
        return ChatClient.builder(openAiChatModel)
                .build();
    }


}
