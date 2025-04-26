package com.hao.topic.model.dto.topic;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/24 23:12
 */
@Data
public class TopicAuditSubject implements Serializable {
    private Long id;
    private String subjectName;
    private String categoryName;
    private String subjectDesc;
    private String account;
    private Long userId;
}
