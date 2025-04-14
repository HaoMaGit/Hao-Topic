package com.hao.topic.model.dto.topic;

import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/14 21:43
 */
@Data
public class TopicSubjectListDto {

    private String subjectName;
    private String createBy;
    private String beginCreateTime;
    private String endCreateTime;
    private String beginUpdateTime;
    private String endUpdateTime;
    private Integer pageNum;
    private Integer pageSize;
}
