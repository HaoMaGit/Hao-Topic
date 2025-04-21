package com.hao.topic.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.topic.ai.constant.AiConstant;
import com.hao.topic.ai.constant.PromptConstant;
import com.hao.topic.ai.mapper.AiHistoryMapper;
import com.hao.topic.ai.service.ModelService;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.model.dto.ai.ChatDto;
import com.hao.topic.model.entity.ai.AiHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/20 14:45
 */
@Service
@Slf4j
public class ModelServiceImpl implements ModelService {
    private final ChatClient chatClient;
    @Autowired
    private AiHistoryMapper aiHistoryMapper;

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
        // 校验模式
        if (chatDto.getModel().equals(AiConstant.SYSTEM_MODEL)) {
            // 系统模式
            return systemModel(chatDto);

        } else if (chatDto.getModel().equals(AiConstant.AI_MODEL)) {
            // AI模式
        }
        // 混合模式
        return this.chatClient.prompt()
                .user(chatDto.getPrompt())
                .stream()
                .content();
    }


    /**
     * 系统模式
     *
     * @param chatDto
     * @return
     */
    private Flux<String> systemModel(ChatDto chatDto) {
        // 获取当前用户Id
        Long currentId = SecurityUtils.getCurrentId();
        // 当前账户
        String currentName = SecurityUtils.getCurrentName();
        // 提示词
        String prompt = null;
        // 判断是否为第一次
        if (chatDto.getMemoryId() == 1) {
            prompt = PromptConstant.INTRODUCTION + "用户输入：" + chatDto.getPrompt();
        } else {
            // 分页
            Page<AiHistory> aiHistoryPage = new Page<>(1, 1);
            // 添加上一次对话记忆 查询上一条数据
            LambdaQueryWrapper<AiHistory> aiHistoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
            aiHistoryLambdaQueryWrapper.eq(AiHistory::getChatId, chatDto.getChatId());
            aiHistoryLambdaQueryWrapper.orderByDesc(AiHistory::getCreateTime);
            aiHistoryLambdaQueryWrapper.eq(AiHistory::getUserId, currentId);
            aiHistoryLambdaQueryWrapper.eq(AiHistory::getAccount, currentName);
            Page<AiHistory> aiHistoryPageDb = aiHistoryMapper.selectPage(aiHistoryPage, aiHistoryLambdaQueryWrapper);
            if (aiHistoryPageDb.getRecords().size() > 0) {
                AiHistory aiHistoryDb = aiHistoryPageDb.getRecords().get(0);
                prompt = "你提出面试题：" + aiHistoryDb.getContent()
                        + "用户回答：" + chatDto.getPrompt() + "  " + PromptConstant.EVALUATE
                        + "继续提问下一道题";
            }
        }
        // 拼接信息
        StringBuffer fullReply = new StringBuffer();
        // 封装返回数据
        AiHistory aiHistory = new AiHistory();
        Flux<String> content = this.chatClient.prompt()
                .user(prompt)
                .stream()
                .content();
        Flux<String> stringFlux = content.flatMap(response -> {
            fullReply.append(response);
            return Flux.just(response);
        }).doOnComplete(() -> {
            log.info("执行完成保存历史记录");
            // 获取当前对话id
            String chatId = chatDto.getChatId();
            aiHistory.setChatId(chatId);
            aiHistory.setAccount(currentName);
            aiHistory.setUserId(currentId);
            aiHistory.setContent(fullReply.toString());
            aiHistory.setTitle(chatDto.getPrompt());
            // TODO 分割到状态
            // aiHistory.setAccuracy();
            aiHistoryMapper.insert(aiHistory);
        });
        return stringFlux;
    }

}
