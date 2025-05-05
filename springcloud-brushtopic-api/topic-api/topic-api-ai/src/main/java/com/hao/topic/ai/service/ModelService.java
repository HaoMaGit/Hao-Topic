package com.hao.topic.ai.service;

import com.hao.topic.model.dto.ai.AiHistoryDto;
import com.hao.topic.model.dto.ai.ChatDto;
import com.hao.topic.model.dto.ai.TtsDto;
import com.hao.topic.model.dto.audit.TopicAudit;
import com.hao.topic.model.dto.audit.TopicAuditCategory;
import com.hao.topic.model.dto.audit.TopicAuditLabel;
import com.hao.topic.model.dto.audit.TopicAuditSubject;
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

    ResponseEntity<byte[]> tts(TtsDto text);

    void deleteHistory(Long id);


    void updateHistoryById(AiHistoryDto aiHistoryDto);

    void auditSubject(TopicAuditSubject topicSubject);

    void auditCategory(TopicAuditCategory topicCategoryDto);

    void recordAuditLog(String content, String account, Long userId);

    void auditLabel(TopicAuditLabel topicAuditLabel);

    void auditTopic(TopicAudit topicAudit);

    void generateAnswer(TopicAudit topicAudit);

    Long count(String date);
}
