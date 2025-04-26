package com.hao.topic.ai.service;

import com.hao.topic.model.dto.ai.AiHistoryDto;
import com.hao.topic.model.dto.ai.ChatDto;
import com.hao.topic.model.dto.topic.TopicAuditCategory;
import com.hao.topic.model.dto.topic.TopicAuditSubject;
import com.hao.topic.model.vo.ai.AiHistoryContent;
import com.hao.topic.model.vo.ai.AiHistoryListVo;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/20 14:45
 */
public interface ModelService {


    Flux<String> chat(ChatDto chatDto);

    List<AiHistoryListVo> getHistory(AiHistoryDto aiHistoryDto);

    List<AiHistoryContent> getHistoryById(Long id);

    ResponseEntity<byte[]> tts(String text);

    void deleteHistory(Long id);


    void updateHistoryById(AiHistoryDto aiHistoryDto);

    void auditSubject(TopicAuditSubject topicSubject);

    void auditCategory(TopicAuditCategory topicCategoryDto);

    void recordAuditLog(String content, String account, Long userId);

}
