package com.hao.topic.security.utils;

import com.hao.topic.security.constant.EmailConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;


/**
 * Description: 发送邮箱验证码工具类
 * Author: Hao
 * Date: 2025/5/2 18:21
 */

@Service
public class EmailSendUtils {
    // 注入JavaMailSender接口
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RedisTemplate redisTemplate;

    // 通过value注解得到配置文件中发送者的邮箱
    @Value("${spring.mail.username}")
    private String userName;// 用户发送者

    // 邮箱验证码 定义为StringBuilder对于增删改操作有优势
    private final StringBuilder EMAIL_CODE = new StringBuilder();

    /**
     * 创建一个发送邮箱验证的方法
     *
     * @param to
     */
    public void sendVerificationEmail(String to) {
        try {
            // 定义email信息格式
            SimpleMailMessage message = new SimpleMailMessage();
            // 调用生成6位数字和字母的方法，生成验证码，该方法在下面定义好了
            generateRandomCode(to);
            // 设置发件人
            message.setFrom(userName);
            // 接收者邮箱，为调用本方法传入的接收者的邮箱xxx@qq.com
            message.setTo(to);
            // 邮件主题
            message.setSubject(EmailConstant.EMAIL_TITLE.getValue());
            // 邮件内容  设置的邮件内容，这里我使用了常量类字符串，加上验证码，再加上常量类字符串
            message.setText(EmailConstant.EMAIL_MESSAGE.getValue() + EMAIL_CODE + EmailConstant.EMAIL_OUT_TIME.getValue());
            // 开始发送
            mailSender.send(message);
        } finally {
            // 发送完了之后，将EMAIL_CODE设置为空
            EMAIL_CODE.setLength(0);
        }
    }

    /**
     * 随机生成6位字母加数字组合的验证码
     *
     * @return
     */
    public void generateRandomCode(String email) {
        // 字母和数字组合
        String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        // 拆分每一个字符放到数组中
        String[] newStr = str.split("");
        // 循环随机生成6为数字和字母组合
        for (int i = 0; i < 6; i++) {
            // 通过循环6次，为stringBuilder追加内容，内容为随机数✖62，取整
            EMAIL_CODE.append(newStr[(int) (Math.random() * 62)]);
        }
        // 存入Redis中并设置时长为2分钟
        redisTemplate.opsForValue().set(EmailConstant.EMAIL_CODE.getValue() + ":" + email, EMAIL_CODE, 1, TimeUnit.MINUTES);
    }
}