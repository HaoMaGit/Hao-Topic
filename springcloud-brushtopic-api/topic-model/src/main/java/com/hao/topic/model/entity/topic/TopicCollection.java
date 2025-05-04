package com.hao.topic.model.entity.topic;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/4 17:21
 */
@Data
public class TopicCollection {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private Long topicId;
}
