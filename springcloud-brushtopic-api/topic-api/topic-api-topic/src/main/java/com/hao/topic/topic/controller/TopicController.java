package com.hao.topic.topic.controller;

import com.hao.topic.api.utils.utils.ExcelUtil;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.result.Result;
import com.hao.topic.model.dto.topic.TopicCategoryDto;
import com.hao.topic.model.dto.topic.TopicCategoryListDto;
import com.hao.topic.model.dto.topic.TopicDto;
import com.hao.topic.model.dto.topic.TopicListDto;
import com.hao.topic.model.excel.topic.TopicCategoryExcel;
import com.hao.topic.topic.service.TopicService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
     * 添加题目分类
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
}
