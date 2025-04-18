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
 * Description:
 * Author: Hao
 * Date: 2025/4/18 11:25
 */
@ContentRowHeight(30)
// 表头行高20
@HeadRowHeight(20)
// 列宽25
@ColumnWidth(25)
// 对齐方式居中
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
@Data
public class TopicExcelExport {
    @ExcelProperty(value = "ID")
    private Long id;
    @ExcelProperty(value = "题目名称")
    private String topicName;
    @ColumnWidth(40)
    @ExcelProperty(value = "题目答案")
    private String answer;
    @ExcelProperty(value = "题目AI答案")
    private String aiAnswer;
    @ExcelProperty(value = "题目排序")
    private Integer sorted;
    @ExcelProperty(value = "题目状态")
    private String status;
    @ColumnWidth(50)
    @ExcelProperty(value = "每日推荐 0不是 1是")
    private Integer isEveryday;
    @ColumnWidth(50)
    @ExcelProperty(value = "会员专享 0不是 1是")
    private Integer isMember;
    @ColumnWidth(30)
    @ExcelProperty(value = "题目专题")
    private String subjectName;
    @ColumnWidth(30)
    @ExcelProperty(value = "题目标签格式(label1:label2:label3等等")
    private String labelName;

}
