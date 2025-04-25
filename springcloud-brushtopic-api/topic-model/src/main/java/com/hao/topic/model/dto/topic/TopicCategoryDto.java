package com.hao.topic.model.dto.topic;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/13 20:16
 */
@Data
public class TopicCategoryDto implements Serializable {
    private String categoryName;
    private Long id;
}
