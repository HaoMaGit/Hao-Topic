package com.hao.topic.ai.service;

import com.hao.topic.model.dto.ai.AiUserDto;

import java.util.Map;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/18 22:33
 */
public interface ManageService {
    Map<String, Object> list(AiUserDto aiUserDto);
}
