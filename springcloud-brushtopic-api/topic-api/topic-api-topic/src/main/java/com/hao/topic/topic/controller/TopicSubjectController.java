package com.hao.topic.topic.controller;

import com.hao.topic.api.utils.helper.MinioHelper;
import com.hao.topic.api.utils.utils.ExcelUtil;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.result.Result;
import com.hao.topic.model.dto.topic.TopicCategoryDto;
import com.hao.topic.model.dto.topic.TopicCategoryListDto;
import com.hao.topic.model.dto.topic.TopicSubjectDto;
import com.hao.topic.model.dto.topic.TopicSubjectListDto;
import com.hao.topic.model.entity.topic.TopicCategory;
import com.hao.topic.model.entity.topic.TopicSubject;
import com.hao.topic.model.excel.topic.TopicCategoryExcel;
import com.hao.topic.model.excel.topic.TopicCategoryExcelExport;
import com.hao.topic.model.excel.topic.TopicSubjectExcel;
import com.hao.topic.model.excel.topic.TopicSubjectExcelExport;
import com.hao.topic.model.vo.topic.TopicSubjectVo;
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
 * Description: 题目专题控制器
 * Author: Hao
 * Date: 2025/4/14 10:37
 */
@RestController
@RequestMapping("/topic/subject")
@AllArgsConstructor
public class TopicSubjectController {

    private final TopicSubjectService topicSubjectService;

    private final MinioHelper minioHelper;

    /**
     * 获取题目专题列表
     *
     * @param topicSubjectListDto
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result<Map<String, Object>> list(TopicSubjectListDto topicSubjectListDto) {
        Map<String, Object> map = topicSubjectService.subjectList(topicSubjectListDto);
        return Result.success(map);
    }

    /**
     * 添加题目专题
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result add(@RequestBody TopicSubjectDto topicCategoryDto) {
        topicSubjectService.add(topicCategoryDto);
        return Result.success();
    }

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/img")
    @PreAuthorize("hasAuthority('admin')  || hasAuthority('member')")
    public Result upload(@RequestParam("avatar") MultipartFile file) {
        // 上传文件
        String url = minioHelper.uploadFile(file, "subject");
        return Result.success(url);
    }


    /**
     * 修改题目专题
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result update(@RequestBody TopicSubjectDto topicSubjectDto) {
        topicSubjectService.update(topicSubjectDto);
        return Result.success();
    }


    /**
     * 审核修改题目专题
     */
    @PutMapping("/audit")
    public void auditSubject(@RequestBody TopicSubject topicSubject) {
        topicSubjectService.auditSubject(topicSubject);
    }

    /**
     * 删除题目专题
     */
    @DeleteMapping("/delete/{ids}")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result delete(@PathVariable Long[] ids) {
        topicSubjectService.delete(ids);
        return Result.success();
    }

    /**
     * 导出excel
     *
     * @param response
     */
    @GetMapping("/export/{ids}")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public void exportExcel(HttpServletResponse response, TopicSubjectListDto topicSubjectListDto, @PathVariable(required = false) Long[] ids) {
        List<TopicSubjectExcelExport> topicSubjectExcelExports = topicSubjectService.getExcelVo(topicSubjectListDto, ids);
        if (CollectionUtils.isEmpty(topicSubjectExcelExports)) {
            throw new TopicException(ResultCodeEnum.EXPORT_ERROR);
        }
        // 导出
        try {
            ExcelUtil.download(response, topicSubjectExcelExports, TopicSubjectExcelExport.class);
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
            String s = topicSubjectService.importExcel(multipartFile, updateSupport);
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
        List<TopicSubjectExcel> excelVoList = new ArrayList<>();
        // 组成模板数据
        TopicSubjectExcel excelVo = new TopicSubjectExcel();
        // 存放
        excelVoList.add(excelVo);
        try {
            // 导出
            ExcelUtil.download(response, excelVoList, TopicSubjectExcel.class);
        } catch (IOException e) {
            throw new TopicException(ResultCodeEnum.DOWNLOAD_ERROR);
        }

    }

    /**
     * 查询所有的专题名称以及id
     *
     * @return
     */
    @GetMapping("/getSubject")
    public Result<List<TopicSubjectVo>> getSubject() {
        List<TopicSubjectVo> list = topicSubjectService.list();
        return Result.success(list);
    }


}
