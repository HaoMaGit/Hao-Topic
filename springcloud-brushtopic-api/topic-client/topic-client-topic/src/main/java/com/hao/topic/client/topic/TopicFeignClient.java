package com.hao.topic.client.topic;

import com.hao.topic.common.auth.TokenInterceptor;
import com.hao.topic.model.entity.topic.Topic;
import com.hao.topic.model.entity.topic.TopicCategory;
import com.hao.topic.model.entity.topic.TopicLabel;
import com.hao.topic.model.entity.topic.TopicSubject;
import com.hao.topic.model.vo.topic.TopicSubjectVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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

    /**
     * 审核题目
     *
     * @param topic
     */
    @PutMapping("/topic/topic/audit")
    void auditTopic(@RequestBody Topic topic);

    /**
     * 修改答案
     *
     * @param topic
     */
    @PutMapping("/topic/topic/answer")
    void updateAiAnswer(@RequestBody Topic topic);


    /**
     * ai查询全部专题或者会员专题
     */
    @GetMapping("/topic/subject/ai/subject/{role}/{createBy}")
    public List<TopicSubjectVo> getSubject(@PathVariable String role, @PathVariable String createBy);
}
