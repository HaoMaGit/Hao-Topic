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
 * Date: 2025/4/13 21:52
 */
@ContentRowHeight(30)
// 表头行高20
@HeadRowHeight(20)
// 列宽25
@ColumnWidth(25)
// 对齐方式居中
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
@Data
public class TopicCategoryExcel {

    @ExcelProperty(value = "题目分类名称")
    private String categoryName;



}
