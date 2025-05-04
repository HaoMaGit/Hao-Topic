package com.hao.topic.model.entity.topic;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
