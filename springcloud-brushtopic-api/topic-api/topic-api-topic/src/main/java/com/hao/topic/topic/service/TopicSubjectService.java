package com.hao.topic.topic.service;

import com.hao.topic.model.dto.topic.TopicSubjectDto;
import com.hao.topic.model.dto.topic.TopicSubjectListDto;
import com.hao.topic.model.entity.topic.TopicSubject;
import com.hao.topic.model.excel.topic.TopicSubjectExcelExport;
import com.hao.topic.model.vo.topic.TopicSubjectVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/14 10:40
 */
public interface TopicSubjectService {
    Map<String, Object> subjectList(TopicSubjectListDto topicSubjectListDto);

    void add(TopicSubjectDto topicCategoryDto);


    void update(TopicSubjectDto topicSubjectDto);

    void delete(Long[] ids);

    List<TopicSubjectExcelExport> getExcelVo(TopicSubjectListDto topicSubjectListDto, Long[] ids);

    String importExcel(MultipartFile multipartFile, Boolean updateSupport) throws IOException;

    List<TopicSubjectVo> list();


    void auditSubject(TopicSubject topicSubject);

}
