package com.hao.topic.model.entity.ai;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hao.topic.model.entity.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: Ai用户表
 * Author: Hao
 * Date: 2025/4/19 11:34
 */
@Data
public class AiUser extends BaseEntity {
    // 账户
    private String account;
    // 用户id
    private Long userId;
    // ai使用次数
    private Long aiCount;
    // ai总次数
    private Long count;
    // 最近使用时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recentlyUsedTime;
    // 状态
    private Integer status;
    private String roleName;
}
