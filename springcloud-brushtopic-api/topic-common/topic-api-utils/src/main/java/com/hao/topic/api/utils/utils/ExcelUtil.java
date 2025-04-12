package com.hao.topic.api.utils.utils;


import com.alibaba.excel.EasyExcel;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 工具类
 */
public class ExcelUtil {

    /**
     * 导出excel
     *
     * @param response
     * @return
     */
    public static <T> void download(HttpServletResponse response, List<T> list, Class<T> clazz) throws IOException {
        // 设置了响应类型
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        // 这里URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode("导出", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        // 告诉浏览器需要下载
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        // 下载excel
        EasyExcel
                // 使用输出字节码文件流
                .write(response.getOutputStream(), clazz)
                // 创建工作表
                .sheet()
                // 写入数据
                .doWrite(list);

    }
}