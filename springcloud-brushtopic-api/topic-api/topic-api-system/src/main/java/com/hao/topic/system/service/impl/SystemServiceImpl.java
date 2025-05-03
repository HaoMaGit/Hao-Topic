package com.hao.topic.system.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hao.topic.common.utils.JWTUtils;
import com.hao.topic.model.entity.system.WebConfig;
import com.hao.topic.model.vo.system.UserInfoVo;
import com.hao.topic.system.constant.SystemConstant;
import com.hao.topic.system.mapper.WebConfigMapper;
import com.hao.topic.system.service.SystemService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
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

    @Autowired
    private WebConfigMapper webConfigMapper;

    public void getCode(HttpServletResponse response) {
        // 生成随机6位
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", 4);
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(SystemConstant.IMAGE_WIDTH, SystemConstant.IMAGE_HEIGHT);
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
            stringRedisTemplate.opsForValue().set(lineCaptcha.getCode(), lineCaptcha.getCode(), SystemConstant.CODE_EXPIRE_TIME, TimeUnit.SECONDS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询前端配置
     *
     * @param status
     * @return
     */
    public WebConfig getConfig(Integer status) {
        LambdaQueryWrapper<WebConfig> webConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();
        webConfigLambdaQueryWrapper.eq(WebConfig::getStatus, status);
        return webConfigMapper.selectOne(webConfigLambdaQueryWrapper);
    }


}
