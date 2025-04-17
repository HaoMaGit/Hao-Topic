package com.hao.topic.topic.service;

import com.hao.topic.model.dto.topic.TopicDto;
import com.hao.topic.model.dto.topic.TopicListDto;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/2 17:47
 */
public interface TopicService {

    Map<String, Object> topicList(TopicListDto topicListDto);

    void add(TopicDto topicDto);

    void update(TopicDto topicDto);

}
