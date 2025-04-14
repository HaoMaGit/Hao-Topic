package com.hao.topic.topic.service;

import com.hao.topic.model.dto.topic.TopicSubjectDto;
import com.hao.topic.model.dto.topic.TopicSubjectListDto;

import java.util.Map;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/14 10:40
 */
public interface TopicSubjectService {
    Map<String, Object> subjectList(TopicSubjectListDto topicSubjectListDto);

    void add(TopicSubjectDto topicCategoryDto);


}
