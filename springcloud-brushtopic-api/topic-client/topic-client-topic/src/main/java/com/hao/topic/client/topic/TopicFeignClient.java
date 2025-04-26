package com.hao.topic.client.topic;

import com.hao.topic.common.auth.TokenInterceptor;
import com.hao.topic.model.entity.topic.TopicCategory;
import com.hao.topic.model.entity.topic.TopicLabel;
import com.hao.topic.model.entity.topic.TopicSubject;
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
    public void auditCategory(@RequestBody TopicCategory topicCategory);

    /**
     * 审核专题
     */
    @PutMapping("/topic/subject/audit")
    public void auditSubject(@RequestBody TopicSubject topicSubject);

    /**
     * 审核标签名称
     *
     * @param topicLabel
     */
    @PutMapping("/topic/label/audit")
    void auditLabel(@RequestBody TopicLabel topicLabel);
}
