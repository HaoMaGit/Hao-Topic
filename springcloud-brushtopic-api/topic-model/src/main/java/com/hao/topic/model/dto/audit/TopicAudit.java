package com.hao.topic.model.dto.audit;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/26 16:43
 */
@Data
public class TopicAudit implements Serializable {
    private String topicName;
    private Long id;
    private String answer;
    private String account;
    private Long userId;
    private String topicSubjectName;
    private String topicLabelName;
}
