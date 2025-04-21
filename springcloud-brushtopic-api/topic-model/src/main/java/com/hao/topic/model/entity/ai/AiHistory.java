package com.hao.topic.model.entity.ai;

import com.hao.topic.model.entity.BaseEntity;
import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/21 21:21
 */
@Data
public class AiHistory extends BaseEntity {
    private Long userId;
    private String account;
    private String title;
    private String content;
    private String chatId;
    private Integer accuracy;

}
