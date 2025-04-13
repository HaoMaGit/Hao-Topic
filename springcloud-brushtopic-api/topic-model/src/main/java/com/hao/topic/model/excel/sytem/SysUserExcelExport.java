package com.hao.topic.model.excel.sytem;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: 导出用户数据
 * Author: Hao
 * Date: 2025/4/13 11:34
 */
@ContentRowHeight(30)
// 表头行高20
@HeadRowHeight(20)
// 列宽25
@ColumnWidth(25)
// 对齐方式居中
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
@Data
public class SysUserExcelExport {
    @ExcelProperty(value = "ID")
    private Long id;
    @ExcelProperty(value = "账户")
    private String account;

    @ExcelProperty(value = "邮箱")
    private String email;

    @ExcelProperty(value = "角色")
    private String roleName;


    @ExcelProperty(value = "注册时间")
    private LocalDateTime createTime;

    @ExcelProperty(value = "会员时间")
    private LocalDateTime memberTime;
}
