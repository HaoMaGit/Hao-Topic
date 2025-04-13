package com.hao.topic.topic.service;

import com.hao.topic.model.dto.topic.TopicCategoryDto;
import com.hao.topic.model.dto.topic.TopicCategoryListDto;

import java.util.Map;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/13 18:26
 */
public interface TopicCategoryService {
    Map<String, Object> categoryList(TopicCategoryListDto topicCategoryDto);

    void add(TopicCategoryDto topicCategoryDto);

    void update(TopicCategoryDto topicCategoryDto);

    void delete(Long[] ids);

}
