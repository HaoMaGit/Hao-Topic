package com.hao.topic.model.entity.topic;

import com.hao.topic.model.entity.BaseEntity;
import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/16 21:35
 */
@Data
public class Topic extends BaseEntity {
    private String topicName;
    private String answer;
    private String aiAnswer;
    private Integer sorted;
    private Integer isEveryday;
    private Integer isMember;
    private Long viewCount;
    private Integer status;
    private String createBy;
    private String failMsg;
}
