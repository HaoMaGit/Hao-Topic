package com.hao.topic.model.entity.topic;

import com.hao.topic.model.entity.BaseEntity;
import lombok.Data;

/**
 * Description: 题目分类
 * Author: Hao
 * Date: 2025/4/13 18:20
 */
@Data
public class TopicCategory extends BaseEntity {
    private String categoryName;
    private Integer status;
    private String createBy;
    private Long subjectCount;
}
