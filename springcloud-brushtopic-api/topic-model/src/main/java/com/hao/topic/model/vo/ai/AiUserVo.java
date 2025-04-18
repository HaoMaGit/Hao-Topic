package com.hao.topic.model.vo.ai;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: 用户使用次数管理
 * Author: Hao
 * Date: 2025/4/18 22:26
 */
@Data
public class AiUserVo {
    private Long id;
    // 账户
    private String account;
    // 邮箱
    private String email;
    // ai使用次数
    private Long aiCount;
    // ai总次数
    private Long count;
    // 最近使用时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recentlyUsedTime;

}
