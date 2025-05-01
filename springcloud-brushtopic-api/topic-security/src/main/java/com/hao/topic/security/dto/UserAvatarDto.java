package com.hao.topic.security.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/1 14:00
 */
@Data
public class UserAvatarDto {

    private String avatar;
    private Long id;
}
