package com.hao.topic.model.entity.topic;

import com.hao.topic.model.entity.BaseEntity;
import lombok.Data;

/**
 * Description: 题目专题与题目的关系表
 * Author: Hao
 * Date: 2025/4/14 22:41
 */
@Data
public class TopicSubjectTopic extends BaseEntity {
    private Long topicId;
    private Long subjectId;
}
