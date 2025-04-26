package com.hao.topic.model.dto.audit;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/26 9:47
 */
@Data
public class TopicAuditCategory implements Serializable {
    private String categoryName;
    private Long id;
    private String account;
    private Long userId;
}
