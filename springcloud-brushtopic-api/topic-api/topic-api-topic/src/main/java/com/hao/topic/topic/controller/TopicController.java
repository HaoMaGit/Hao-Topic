package com.hao.topic.topic.controller;

import com.hao.topic.api.utils.utils.ExcelUtil;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.result.Result;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.model.dto.topic.TopicCategoryDto;
import com.hao.topic.model.dto.topic.TopicCategoryListDto;
import com.hao.topic.model.dto.topic.TopicDto;
import com.hao.topic.model.dto.topic.TopicListDto;
import com.hao.topic.model.entity.topic.Topic;
import com.hao.topic.model.excel.topic.*;
import com.hao.topic.topic.service.TopicService;
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
 * Description: 题目控制层
 * Author: Hao
 * Date: 2025/4/16 20:08
 */
@RestController
@RequestMapping("/topic/topic")
@AllArgsConstructor
public class TopicController {
    private final TopicService topicService;

    /**
     * 获取题目列表
     *
     * @param topicListDto
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result<Map<String, Object>> list(TopicListDto topicListDto) {
        Map<String, Object> map = topicService.topicList(topicListDto);
        return Result.success(map);
    }

    /**
     * 添加题目
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result add(@RequestBody TopicDto topicDto) {
        topicService.add(topicDto);
        return Result.success();
    }


    /**
     * 修改题目
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result update(@RequestBody TopicDto topicDto) {
        topicService.update(topicDto);
        return Result.success();
    }

    /**
     * 删除题目
     */
    @DeleteMapping("/delete/{ids}")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result delete(@PathVariable Long[] ids) {
        topicService.delete(ids);
        return Result.success();
    }


    /**
     * 导出excel
     *
     * @param response
     */
    @GetMapping("/export/{ids}")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public void exportExcel(HttpServletResponse response, TopicListDto topicListDto, @PathVariable(required = false) Long[] ids) {
        List<TopicExcelExport> topicExcelExports = topicService.getExcelVo(topicListDto, ids);
        if (CollectionUtils.isEmpty(topicExcelExports)) {
            throw new TopicException(ResultCodeEnum.EXPORT_ERROR);
        }
        // 导出
        try {
            ExcelUtil.download(response, topicExcelExports, TopicExcelExport.class);
        } catch (IOException e) {
            throw new TopicException(ResultCodeEnum.EXPORT_ERROR);
        }
    }


    /**
     * 导入excel会员
     */
    @PostMapping("/memberImport")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result<String> memberImport(@RequestParam("file") MultipartFile multipartFile, @RequestParam("updateSupport") Boolean updateSupport) {
        try {
            // 导入数据
            String s = topicService.memberImport(multipartFile, updateSupport);
            return Result.success(s);
        } catch (Exception e) {
            return Result.fail(e.getMessage(), ResultCodeEnum.IMPORT_ERROR);
        }
    }

    /**
     * 导入excel管理员
     */
    @PostMapping("/adminImport")
    @PreAuthorize("hasAuthority('admin')")
    public Result<String> adminImport(@RequestParam("file") MultipartFile multipartFile, @RequestParam("updateSupport") Boolean updateSupport) {
        try {
            // 导入数据
            String s = topicService.adminImport(multipartFile, updateSupport);
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
        // 获取当前用户身份
        String role = SecurityUtils.getCurrentRole();
        if (role.equals("admin")) {
            List<TopicExcel> topicExcels = new ArrayList<>();
            // 组成模板数据
            TopicExcel topicExcel = new TopicExcel();
            // 存放
            topicExcels.add(topicExcel);
            try {
                // 导出
                ExcelUtil.download(response, topicExcels, TopicExcel.class);
            } catch (IOException e) {
                throw new TopicException(ResultCodeEnum.DOWNLOAD_ERROR);
            }
        } else {
            // 存储模板数据
            List<TopicMemberExcel> excelVoList = new ArrayList<>();
            // 组成模板数据
            TopicMemberExcel excelVo = new TopicMemberExcel();
            // 存放
            excelVoList.add(excelVo);
            try {
                // 导出
                ExcelUtil.download(response, excelVoList, TopicMemberExcel.class);
            } catch (IOException e) {
                throw new TopicException(ResultCodeEnum.DOWNLOAD_ERROR);
            }
        }
    }

    /**
     * 审核题目
     *
     * @param topic
     */
    @PutMapping("/audit")
    public void auditTopic(@RequestBody Topic topic) {
        topicService.auditTopic(topic);
    }

    /**
     * 发送消息生成ai答案
     */
    @GetMapping("/generateAnswer/{id}")
    public void generateAnswer(@PathVariable Long id) {
        topicService.generateAnswer(id);
    }

    /**
     * 修改ai答案
     */
    @PutMapping("/answer")
    void updateAiAnswer(@RequestBody Topic topic) {
        topicService.updateAiAnswer(topic);
    }
}
