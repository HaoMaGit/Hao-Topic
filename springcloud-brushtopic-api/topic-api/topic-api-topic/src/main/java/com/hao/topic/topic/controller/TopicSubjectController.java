package com.hao.topic.topic.controller;

import com.hao.topic.api.utils.helper.MinioHelper;
import com.hao.topic.common.result.Result;
import com.hao.topic.model.dto.topic.TopicCategoryDto;
import com.hao.topic.model.dto.topic.TopicCategoryListDto;
import com.hao.topic.model.dto.topic.TopicSubjectDto;
import com.hao.topic.model.dto.topic.TopicSubjectListDto;
import com.hao.topic.model.entity.topic.TopicSubject;
import com.hao.topic.topic.service.TopicSubjectService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
     * 删除题目专题
     */
    @DeleteMapping("/delete/{ids}")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('member')")
    public Result delete(@PathVariable Long[] ids) {
        topicSubjectService.delete(ids);
        return Result.success();
    }

}
