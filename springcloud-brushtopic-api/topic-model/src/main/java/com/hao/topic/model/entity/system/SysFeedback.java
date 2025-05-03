package com.hao.topic.model.entity.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hao.topic.model.entity.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/2 22:52
 */
@Data
public class SysFeedback extends BaseEntity {

    private String account;
    private Long userId;
    private String feedbackContent;
    private String replyContent;
    private Integer status;
    private String replyAccount;
    private Long replyId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime replyTime;
}
