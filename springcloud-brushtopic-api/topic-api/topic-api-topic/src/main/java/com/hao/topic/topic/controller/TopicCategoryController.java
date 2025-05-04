package com.hao.topic.topic.controller;

import com.alibaba.excel.EasyExcel;
import com.hao.topic.api.utils.utils.ExcelUtil;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.result.Result;
import com.hao.topic.model.dto.topic.TopicCategoryDto;
import com.hao.topic.model.dto.topic.TopicCategoryListDto;
import com.hao.topic.model.entity.topic.TopicCategory;
import com.hao.topic.model.excel.sytem.SysUserExcel;
import com.hao.topic.model.excel.topic.TopicCategoryExcel;
import com.hao.topic.model.excel.topic.TopicCategoryExcelExport;
import com.hao.topic.model.vo.topic.TopicCategoryVo;
import com.hao.topic.topic.service.TopicCategoryService;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: 题目分类控制器
 * Author: Hao
 * Date: 2025/4/13 18:22
 */
@RestController
@RequestMapping("/topic/category")
@AllArgsConstructor
public class TopicCategoryController {

    private final TopicCategoryService topicCategoryService;

    /**
     * 获取题目分类列表
     *
     * @param topicCategoryDto
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result<Map<String, Object>> list(TopicCategoryListDto topicCategoryDto) {
        Map<String, Object> map = topicCategoryService.categoryList(topicCategoryDto);
        return Result.success(map);
    }

    /**
     * 添加题目分类
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result add(@RequestBody TopicCategoryDto topicCategoryDto) {
        topicCategoryService.add(topicCategoryDto);
        return Result.success();
    }

    /**
     * 修改题目分类
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result update(@RequestBody TopicCategoryDto topicCategoryDto) {
        topicCategoryService.update(topicCategoryDto);
        return Result.success();
    }

    /**
     * 审核修改题目分类
     */
    @PutMapping("/audit")
    public void auditCategory(@RequestBody TopicCategory topicCategory) {
        topicCategoryService.auditCategory(topicCategory);
    }

    /**
     * 删除题目分类
     */
    @DeleteMapping("/delete/{ids}")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result delete(@PathVariable Long[] ids) {
        topicCategoryService.delete(ids);
        return Result.success();
    }

    /**
     * 导出excel
     *
     * @param response
     */
    @GetMapping("/export/{ids}")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public void exportExcel(HttpServletResponse response, TopicCategoryListDto topicCategoryListDto, @PathVariable(required = false) Long[] ids) {
        List<TopicCategoryExcelExport> topicCategoryExcelExports = topicCategoryService.getExcelVo(topicCategoryListDto, ids);
        if (CollectionUtils.isEmpty(topicCategoryExcelExports)) {
            throw new TopicException(ResultCodeEnum.EXPORT_ERROR);
        }
        // 导出
        try {
            ExcelUtil.download(response, topicCategoryExcelExports, TopicCategoryExcelExport.class);
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
            String s = topicCategoryService.importExcel(multipartFile, updateSupport);
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
        List<TopicCategoryExcel> excelVoList = new ArrayList<>();
        // 组成模板数据
        TopicCategoryExcel excelVo = new TopicCategoryExcel();
        // 存放
        excelVoList.add(excelVo);
        try {
            // 导出
            ExcelUtil.download(response, excelVoList, TopicCategoryExcel.class);
        } catch (IOException e) {
            throw new TopicException(ResultCodeEnum.DOWNLOAD_ERROR);
        }
    }

    /**
     * h5查询分类名称和id
     */
    @GetMapping("/category/{isCustomQuestion}")
    public Result<List<TopicCategoryVo>> category(@PathVariable Boolean isCustomQuestion) {
        List<TopicCategoryVo> list = topicCategoryService.category(isCustomQuestion);
        return Result.success(list);
    }
}
