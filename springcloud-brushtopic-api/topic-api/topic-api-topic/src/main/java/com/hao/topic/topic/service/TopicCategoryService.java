package com.hao.topic.topic.service;

import com.hao.topic.model.dto.topic.TopicCategoryDto;

import java.util.Map;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/13 18:26
 */
public interface TopicCategoryService {
    Map<String, Object> categoryList(TopicCategoryDto topicCategoryDto);

}
