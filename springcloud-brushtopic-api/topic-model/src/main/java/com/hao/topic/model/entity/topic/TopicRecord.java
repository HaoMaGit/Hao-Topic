package com.hao.topic.model.entity.topic;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hao.topic.model.entity.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Description: 题目记录
 * Author: Hao
 * Date: 2025/5/4 21:57
 */
@Data
public class TopicRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String nickname;
    private Long topicId;
    private Long subjectId;
    private Long count;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
