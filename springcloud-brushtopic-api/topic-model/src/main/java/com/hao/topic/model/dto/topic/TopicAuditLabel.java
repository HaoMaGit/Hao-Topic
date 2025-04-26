package com.hao.topic.model.dto.topic;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/26 13:34
 */
@Data
public class TopicAuditLabel implements Serializable {
    private String labelName;
    private Long id;
    private String account;
    private Long userId;
}
