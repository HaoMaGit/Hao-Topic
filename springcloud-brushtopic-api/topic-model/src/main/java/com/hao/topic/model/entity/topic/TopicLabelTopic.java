package com.hao.topic.model.entity.topic;

import com.hao.topic.model.entity.BaseEntity;
import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/15 21:08
 */
@Data
public class TopicLabelTopic extends BaseEntity {
    private Long topicId;
    private Long labelId;
}
