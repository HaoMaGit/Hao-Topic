package com.hao.topic.model.vo.topic;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/4 18:02
 */
@Data
public class TopicCollectionVo {
    private Long id;
    private String topicName;
    private String collectionTime;
    List<String> labelNames;

}
