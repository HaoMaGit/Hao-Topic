package com.hao.topic.model.dto.topic;

import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/15 21:11
 */
@Data
public class TopicLabelListDto {
    private String labelName;
    private String createBy;
    private String beginCreateTime;
    private String endCreateTime;
    private Integer pageNum;
    private Integer pageSize;
}
