package com.hao.topic.model.entity.ai;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hao.topic.model.entity.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/21 21:21
 */
@Data
public class AiHistory extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
    private Long userId;
    private String account;
    private String title;
    private String content;
    private String chatId;
    private Integer accuracy;
    private Integer parent;

}
