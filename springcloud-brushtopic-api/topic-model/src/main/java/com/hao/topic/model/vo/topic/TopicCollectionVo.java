package com.hao.topic.model.vo.topic;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Long subjectId;
    private String topicName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String collectionTime;
    List<String> labelNames;

}
