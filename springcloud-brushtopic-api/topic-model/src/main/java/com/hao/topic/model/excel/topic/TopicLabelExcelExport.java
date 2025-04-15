package com.hao.topic.model.excel.topic;

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
 * Description:
 * Author: Hao
 * Date: 2025/4/15 22:07
 */
@ContentRowHeight(30)
// 表头行高20
@HeadRowHeight(20)
// 列宽25
@ColumnWidth(25)
// 对齐方式居中
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
@Data
public class TopicLabelExcelExport {

    @ExcelProperty(value = "ID")
    private Long id;

    @ExcelProperty(value = "题目标签名称")
    private String labelName;

    @ExcelProperty(value = "创建人")
    private String createBy;

    @ExcelProperty(value = "状态")
    private String status;

    @ExcelProperty(value = "题目数量")
    private Long useCount;

    @ExcelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ExcelProperty(value = "修改时间")
    private LocalDateTime updateTime;
}
