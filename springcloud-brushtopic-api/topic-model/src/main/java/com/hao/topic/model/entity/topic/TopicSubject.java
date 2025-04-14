package com.hao.topic.model.entity.topic;

import com.hao.topic.model.entity.BaseEntity;
import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/14 10:38
 */
@Data
public class TopicSubject extends BaseEntity {
    private String subjectName;
    private String subjectDesc;
    private String imageUrl;
    private Integer topicCount;
    private String createBy;
    private Long viewCount;
    private Integer status;
}
