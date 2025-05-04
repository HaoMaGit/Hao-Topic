package com.hao.topic.model.vo.system;

import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/3 22:23
 */
@Data
public class SysNoticeVo {
    private Long id;
    private String account;
    private Integer status;
    private String content;
    // 时间描述
    private String timeDesc;
}
