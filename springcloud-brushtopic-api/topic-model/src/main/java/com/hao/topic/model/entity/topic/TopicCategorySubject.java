package com.hao.topic.model.entity.topic;

import com.hao.topic.model.entity.BaseEntity;
import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/13 20:25
 */
@Data
public class TopicCategorySubject extends BaseEntity {

    private Long categoryId;
    private Long subjectId;
}
