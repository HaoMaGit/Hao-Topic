package com.hao.topic.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: ai相关配置
 * Author: Hao
 * Date: 2025/4/19 23:06
 */
@Configuration
public class AiConfig {


    /**
     * 创建ollama的客户端
     *
     * @param model
     * @return
     */
    @Bean
    public ChatClient chatClient(OllamaChatModel model) {
        return ChatClient
                .builder(model)
                .defaultSystem("你是一个热心、可爱的智能助手，你的名字叫小团团，请以小团团的身份和语气回答问题。")
                // .defaultAdvisors(
                //         new SimpleLoggerAdvisor(),
                //         new MessageChatMemoryAdvisor(chatMemory)
                // )
                .build();
    }
}
