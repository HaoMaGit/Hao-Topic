package com.hao.topic.topic.service;

import com.hao.topic.model.dto.topic.TopicLabelDto;
import com.hao.topic.model.dto.topic.TopicLabelListDto;

import java.util.Map;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/15 21:10
 */
public interface TopicLabelService {
    Map<String, Object> labelList(TopicLabelListDto topicLabelListDto);

    void add(TopicLabelDto topicLabelDto);

    void update(TopicLabelDto topicLabelDto);

    void delete(Long[] ids);
}
