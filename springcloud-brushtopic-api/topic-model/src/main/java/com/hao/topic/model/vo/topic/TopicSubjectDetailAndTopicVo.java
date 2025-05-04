package com.hao.topic.model.vo.topic;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/4 14:55
 */
@Data
public class TopicSubjectDetailAndTopicVo {
    private String imageUrl;
    private String subjectName;
    private String subjectDesc;
    private Long topicCount;
    private Long viewCount;
    List<TopicNameVo> topicNameVos;

}
