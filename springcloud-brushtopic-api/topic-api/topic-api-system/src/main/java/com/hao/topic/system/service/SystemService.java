package com.hao.topic.system.service;

import com.hao.topic.model.entity.system.WebConfig;
import com.hao.topic.model.vo.system.UserInfoVo;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/2 19:17
 */
public interface SystemService {
    void getCode(HttpServletResponse response);

    WebConfig getConfig(Integer status);
}
