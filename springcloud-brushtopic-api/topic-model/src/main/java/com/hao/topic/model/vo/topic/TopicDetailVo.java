package com.hao.topic.model.vo.topic;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/4 15:46
 */
@Data
public class TopicDetailVo {

    private String topicName;
    private Boolean isCollected;
    private Integer isMember;
    List<String> labelNames;

}
