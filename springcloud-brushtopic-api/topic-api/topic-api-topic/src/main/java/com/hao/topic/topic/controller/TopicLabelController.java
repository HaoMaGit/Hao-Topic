package com.hao.topic.topic.controller;

import com.hao.topic.api.utils.helper.MinioHelper;
import com.hao.topic.api.utils.utils.ExcelUtil;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.result.Result;
import com.hao.topic.model.dto.topic.TopicLabelDto;
import com.hao.topic.model.dto.topic.TopicLabelListDto;
import com.hao.topic.model.dto.topic.TopicSubjectDto;
import com.hao.topic.model.dto.topic.TopicSubjectListDto;
import com.hao.topic.model.entity.topic.TopicLabel;
import com.hao.topic.model.excel.topic.TopicLabelExcel;
import com.hao.topic.model.excel.topic.TopicLabelExcelExport;
import com.hao.topic.model.vo.topic.TopicLabelVo;
import com.hao.topic.model.vo.topic.TopicSubjectVo;
import com.hao.topic.topic.service.TopicLabelService;
import com.hao.topic.topic.service.TopicSubjectService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description: 题目标签控制器
 * Author: Hao
 * Date: 2025/4/15 21:08
 */
@RestController
@RequestMapping("/topic/label")
@AllArgsConstructor
public class TopicLabelController {

    private final TopicLabelService topicLabelService;


    /**
     * 获取题目标签列表
     *
     * @param topicLabelListDto
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result<Map<String, Object>> list(TopicLabelListDto topicLabelListDto) {
        Map<String, Object> map = topicLabelService.labelList(topicLabelListDto);
        return Result.success(map);
    }

    /**
     * 添加题目标签
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result add(@RequestBody TopicLabelDto topicLabelDto) {
        topicLabelService.add(topicLabelDto);
        return Result.success();
    }


    /**
     * 修改题目标签
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result update(@RequestBody TopicLabelDto topicLabelDto) {
        topicLabelService.update(topicLabelDto);
        return Result.success();
    }

    /**
     * 删除题目标签
     */
    @DeleteMapping("/delete/{ids}")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result delete(@PathVariable Long[] ids) {
        topicLabelService.delete(ids);
        return Result.success();
    }

    /**
     * 导出excel
     *
     * @param response
     */
    @GetMapping("/export/{ids}")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public void exportExcel(HttpServletResponse response, TopicLabelListDto topicLabelListDto, @PathVariable(required = false) Long[] ids) {
        List<TopicLabelExcelExport> topicLabelExcelExport = topicLabelService.getExcelVo(topicLabelListDto, ids);
        if (CollectionUtils.isEmpty(topicLabelExcelExport)) {
            throw new TopicException(ResultCodeEnum.EXPORT_ERROR);
        }
        // 导出
        try {
            ExcelUtil.download(response, topicLabelExcelExport, TopicLabelExcelExport.class);
        } catch (IOException e) {
            throw new TopicException(ResultCodeEnum.EXPORT_ERROR);
        }
    }

    /**
     * 导入excel
     */
    @PostMapping("/import")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result<String> importExcel(@RequestParam("file") MultipartFile multipartFile, @RequestParam("updateSupport") Boolean updateSupport) {
        try {
            // 导入数据
            String s = topicLabelService.importExcel(multipartFile, updateSupport);
            return Result.success(s);
        } catch (Exception e) {
            return Result.fail(e.getMessage(), ResultCodeEnum.IMPORT_ERROR);
        }
    }


    /**
     * 下载excel模板
     */
    @GetMapping("/template")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public void getExcelTemplate(HttpServletResponse response) {
        // 存储模板数据
        List<TopicLabelExcel> excelVoList = new ArrayList<>();
        // 组成模板数据
        TopicLabelExcel excelVo = new TopicLabelExcel();
        // 存放
        excelVoList.add(excelVo);
        try {
            // 导出
            ExcelUtil.download(response, excelVoList, TopicLabelExcel.class);
        } catch (IOException e) {
            throw new TopicException(ResultCodeEnum.DOWNLOAD_ERROR);
        }

    }


    /**
     * 查询所有的标签名称以及id
     *
     * @return
     */
    @GetMapping("/getLabel")
    public Result<List<TopicLabelVo>> getSubject() {
        List<TopicLabelVo> list = topicLabelService.list();
        return Result.success(list);
    }

    /**
     * 审核标签
     *
     * @param topicLabel
     */
    @PutMapping("/audit")
    public void auditLabel(@RequestBody TopicLabel topicLabel) {
        topicLabelService.auditLabel(topicLabel);
    }
}
