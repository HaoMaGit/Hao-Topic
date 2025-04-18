package com.hao.topic.model.dto.ai;

import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/18 22:34
 */
@Data
public class AiUserDto {
    private String account;
    private String beginRecentlyUsedTime;
    private String endRecentlyUsedTime;
    private Integer pageNum;
    private Integer pageSize;
}
