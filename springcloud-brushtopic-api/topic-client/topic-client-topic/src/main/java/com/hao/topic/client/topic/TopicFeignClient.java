package com.hao.topic.client.topic;

import com.hao.topic.common.auth.TokenInterceptor;
import com.hao.topic.model.entity.topic.TopicCategory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/25 22:47
 */
@FeignClient(name = "service-topic", configuration = TokenInterceptor.class)
public interface TopicFeignClient {

    /**
     * 审核分类名称
     *
     * @param topicCategory
     */
    @PutMapping("/topic/category/audit")
    public void audit(@RequestBody TopicCategory topicCategory);
}
