package com.hao.topic.system.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import com.hao.topic.system.service.SystemService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/2 19:17
 */
@Service
public class SystemServiceImpl implements SystemService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void getCode(HttpServletResponse response) {
        // 生成随机6位
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", 4);
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        lineCaptcha.setGenerator(randomGenerator);
        // 重新生成code
        lineCaptcha.createCode();
        // 设置返回数据类型
        response.setContentType("image/jpeg");
        // 禁止使用缓存
        response.setHeader("Pragma", "No-cache");
        try {
            // 输出到页面
            lineCaptcha.write(response.getOutputStream());
            // 关闭流
            response.getOutputStream().close();
            // 将生成的验证码放入redis中
            stringRedisTemplate.opsForValue().set("captcha:" + lineCaptcha.getCode(), lineCaptcha.getCode(), 60, TimeUnit.SECONDS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
