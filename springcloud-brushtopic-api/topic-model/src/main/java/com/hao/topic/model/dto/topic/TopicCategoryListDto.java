package com.hao.topic.model.dto.topic;

import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/13 18:24
 */
@Data
public class TopicCategoryListDto {
    private String categoryName;
    private String createBy;
    private String beginCreateTime;
    private String endCreateTime;
    private Integer status;
    private Integer pageNum;
    private Integer pageSize;
}
