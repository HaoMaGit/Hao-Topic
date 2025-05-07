package com.hao.topic.model.vo.topic;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/7 16:42
 */
@Data
public class TopicTodayVo {
    private Long id;
    private Long topicId;
    private Long subjectId;
    private String topicName;
    private Integer status;
    List<String> labelNames;


}
