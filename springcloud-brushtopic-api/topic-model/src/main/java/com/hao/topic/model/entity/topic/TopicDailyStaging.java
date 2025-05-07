package com.hao.topic.model.entity.topic;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hao.topic.model.entity.BaseEntity;
import lombok.Data;

/**
 * Description: 每日题目暂存表
 * Author: Hao
 * Date: 2025/5/7 14:28
 */
@Data
public class TopicDailyStaging {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private Long topicId;
    private Integer status;
    private Long subjectId;
    private Integer isPublic;
}
