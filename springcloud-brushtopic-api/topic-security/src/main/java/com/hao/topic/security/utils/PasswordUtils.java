package com.hao.topic.security.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Description: 密码工具类
 * Author: Hao
 * Date: 2025/4/13 12:05
 */

public class PasswordUtils {
    /**
     * 加密密码
     * @param password
     * @return
     */
    public static String encodePassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }
}
