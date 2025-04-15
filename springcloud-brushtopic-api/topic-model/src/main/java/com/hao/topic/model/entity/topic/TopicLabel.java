package com.hao.topic.model.entity.topic;

import com.hao.topic.model.entity.BaseEntity;
import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/15 21:06
 */
@Data
public class TopicLabel  extends BaseEntity {
    private String labelName;
    private Integer status;
    private String createBy;
    private Long useCount;
}
