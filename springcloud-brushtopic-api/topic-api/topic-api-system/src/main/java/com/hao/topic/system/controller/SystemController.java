package com.hao.topic.system.controller;

import com.hao.topic.system.service.SystemService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 系统相关接口
 * Author: Hao
 * Date: 2025/4/2 19:16
 */
@RestController
@RequestMapping("system")
public class SystemController {

    @Autowired
    private SystemService service;

    /**
     * 获取验证码
     *
     * @param response
     * @return
     */
    @GetMapping("/captchaImage")
    public void getCode(HttpServletResponse response) {
        service.getCode(response);
    }
}
