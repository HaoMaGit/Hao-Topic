package com.hao.topic.topic.service;

import com.hao.topic.model.entity.topic.Topic;
import com.hao.topic.model.vo.topic.TopicSubjectVo;

import java.util.List;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/5 17:13
 */
public interface TopicAiService {
    List<Topic> getSubjectIdByTopicList(Long subjectId);

    List<TopicSubjectVo> getSubject(String role, String createBy);
}
