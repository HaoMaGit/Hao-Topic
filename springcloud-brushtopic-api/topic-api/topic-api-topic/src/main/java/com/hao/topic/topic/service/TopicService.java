package com.hao.topic.topic.service;

import com.hao.topic.model.dto.topic.TopicDto;
import com.hao.topic.model.dto.topic.TopicListDto;
import com.hao.topic.model.dto.topic.TopicRecordCountDto;
import com.hao.topic.model.entity.topic.Topic;
import com.hao.topic.model.excel.topic.TopicExcelExport;
import com.hao.topic.model.vo.topic.TopicAnswerVo;
import com.hao.topic.model.vo.topic.TopicCollectionVo;
import com.hao.topic.model.vo.topic.TopicDetailVo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/2 17:47
 */
public interface TopicService {

    Map<String, Object> topicList(TopicListDto topicListDto);

    void add(TopicDto topicDto);

    void update(TopicDto topicDto);

    void delete(Long[] ids);

    List<TopicExcelExport> getExcelVo(TopicListDto topicListDto, Long[] ids);


    String memberImport(MultipartFile multipartFile, Boolean updateSupport) throws IOException;

    String adminImport(MultipartFile multipartFile, Boolean updateSupport) throws IOException;

    void auditTopic(Topic topic);

    void generateAnswer(Long id);

    void updateAiAnswer(Topic topic);

    TopicDetailVo detail(Long id);

    TopicAnswerVo getAnswer(Long id);

    void collection(Long id);

    List<TopicCollectionVo> collectionList();

    void count(TopicRecordCountDto topicRecordCountDto);

}
