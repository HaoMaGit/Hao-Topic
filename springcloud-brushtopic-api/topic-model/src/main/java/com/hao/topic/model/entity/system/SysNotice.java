package com.hao.topic.model.entity.system;

import com.hao.topic.model.entity.BaseEntity;
import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/3 16:24
 */
@Data
public class SysNotice extends BaseEntity {

    private Long id;
    private String account;
    private Long userId;
    private String content;
    private Integer status;
    private Integer isRead;
    private Long recipientsId;
}
