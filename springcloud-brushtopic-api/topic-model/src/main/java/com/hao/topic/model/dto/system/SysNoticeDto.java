package com.hao.topic.model.dto.system;

import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/3 16:33
 */
@Data
public class SysNoticeDto {
    private String content;
    private Integer status;
    private Long recipientsId;
}
