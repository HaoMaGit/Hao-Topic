package com.hao.topic.system.service;

import com.hao.topic.model.entity.system.SysFeedback;
import com.hao.topic.model.vo.system.SysFeedbackUserVo;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/2 22:54
 */
public interface SysFeedbackService {
    void saveFeedback(SysFeedback sysFeedback);

    Map<String, Object> list(SysFeedback sysFeedback);

    List<SysFeedbackUserVo> feedback();

}
