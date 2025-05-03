package com.hao.topic.model.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/3 15:04
 */
@Data
public class WebConfig {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String content;
    private Double price;
    private String url;
    private Integer status;
}
