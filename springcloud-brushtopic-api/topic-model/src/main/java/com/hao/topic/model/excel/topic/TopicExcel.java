package com.hao.topic.model.excel.topic;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import lombok.Data;

/**
 * Description: 管理员导入模板
 * Author: Hao
 * Date: 2025/4/18 11:05
 */
@ContentRowHeight(30)
// 表头行高20
@HeadRowHeight(20)
// 列宽25
@ColumnWidth(25)
// 对齐方式居中
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
@Data
public class TopicExcel {
    @ExcelProperty(value = "题目名称")
    private String topicName;
    @ColumnWidth(40)
    @ExcelProperty(value = "题目答案")
    private String answer;
    @ExcelProperty(value = "题目排序")
    private Integer sorted;
    @ExcelProperty(value = "每日推荐 0不是 1是")
    @ColumnWidth(40)
    private Integer isEveryday;
    @ExcelProperty(value = "会员专享 0不是 1是")
    @ColumnWidth(40)
    private Integer isMember;
    @ExcelProperty(value = "题目专题")
    private String subjectName;
    @ColumnWidth(80)
    @ExcelProperty(value = "题目标签格式(label1:label2:label3等等")
    private String labelName;
}
