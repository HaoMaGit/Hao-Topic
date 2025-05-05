package com.hao.topic.security.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;

/**
 * 日期处理工具类
 */
public class DateUtils {
    public static LocalDateTime parseStartOfDay(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateStr, formatter);
        return LocalDateTime.of(date, LocalTime.MIN); // 今天 00:00:00
    }

    public static LocalDateTime parseEndOfDay(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateStr, formatter);
        return LocalDateTime.of(date, LocalTime.MAX); // 今天 23:59:59
    }
}
