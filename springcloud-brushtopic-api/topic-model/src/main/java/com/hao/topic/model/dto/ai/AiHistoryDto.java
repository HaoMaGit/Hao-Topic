package com.hao.topic.model.dto.ai;

import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/22 12:03
 */
@Data
public class AiHistoryDto {
    private Long id;
    private String title;
    private Integer pageNum;
    private Integer pageSize;
}
