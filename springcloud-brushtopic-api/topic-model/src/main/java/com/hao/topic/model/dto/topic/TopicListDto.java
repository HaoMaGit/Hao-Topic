package com.hao.topic.model.dto.topic;

import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/16 21:41
 */
@Data
public class TopicListDto {
    private String topicName;
    private String createBy;
    private String beginCreateTime;
    private String endCreateTime;
    private Integer pageNum;
    private Integer pageSize;
}
