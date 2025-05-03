package com.hao.topic.model.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/3 10:49
 */
@Data
public class SysFeedbackVo {

    private Long id;
    private String account;
    private String feedbackContent;
    private String replyContent;
    private Integer status;
    private String replyAccount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime replyTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
