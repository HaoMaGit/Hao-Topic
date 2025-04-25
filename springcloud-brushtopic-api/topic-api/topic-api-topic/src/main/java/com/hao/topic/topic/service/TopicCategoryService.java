package com.hao.topic.topic.service;

import com.hao.topic.model.dto.topic.TopicCategoryDto;
import com.hao.topic.model.dto.topic.TopicCategoryListDto;
import com.hao.topic.model.entity.topic.TopicCategory;
import com.hao.topic.model.excel.topic.TopicCategoryExcelExport;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/13 18:26
 */
public interface TopicCategoryService {
    Map<String, Object> categoryList(TopicCategoryListDto topicCategoryDto);

    void add(TopicCategoryDto topicCategoryDto);

    void update(TopicCategoryDto topicCategoryDto);

    void delete(Long[] ids);

    List<TopicCategoryExcelExport> getExcelVo(TopicCategoryListDto sysUserListDto, Long[] ids);

    String importExcel(MultipartFile multipartFile, Boolean updateSupport) throws IOException;

    void auditCategory(TopicCategory topicCategory);

}
