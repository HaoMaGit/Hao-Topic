package com.hao.topic.topic.service;

import com.hao.topic.model.dto.topic.TopicLabelDto;
import com.hao.topic.model.dto.topic.TopicLabelListDto;
import com.hao.topic.model.entity.topic.TopicLabel;
import com.hao.topic.model.excel.topic.TopicLabelExcelExport;
import com.hao.topic.model.vo.topic.TopicLabelVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/15 21:10
 */
public interface TopicLabelService {
    Map<String, Object> labelList(TopicLabelListDto topicLabelListDto);

    void add(TopicLabelDto topicLabelDto);

    void update(TopicLabelDto topicLabelDto);

    void delete(Long[] ids);

    List<TopicLabelExcelExport> getExcelVo(TopicLabelListDto topicLabelDto, Long[] ids);

    String importExcel(MultipartFile multipartFile, Boolean updateSupport) throws IOException;

    List<TopicLabelVo> list();


    void auditLabel(TopicLabel topicLabel);
}
