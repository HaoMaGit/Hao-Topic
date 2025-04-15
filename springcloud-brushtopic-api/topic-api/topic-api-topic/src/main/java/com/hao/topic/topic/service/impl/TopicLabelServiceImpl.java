package com.hao.topic.topic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.common.utils.StringUtils;
import com.hao.topic.model.dto.topic.TopicLabelDto;
import com.hao.topic.model.dto.topic.TopicLabelListDto;
import com.hao.topic.model.entity.topic.*;
import com.hao.topic.model.entity.topic.TopicLabel;
import com.hao.topic.model.entity.topic.TopicLabel;
import com.hao.topic.topic.enums.StatusEnums;
import com.hao.topic.topic.mapper.TopicLabelMapper;
import com.hao.topic.topic.mapper.TopicLabelTopicMapper;
import com.hao.topic.topic.mapper.TopicSubjectTopicMapper;
import com.hao.topic.topic.service.TopicLabelService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/15 21:10
 */
@Service
@AllArgsConstructor
@Slf4j
public class TopicLabelServiceImpl implements TopicLabelService {

    private final TopicLabelMapper topicLabelMapper;
    private final TopicLabelTopicMapper topicLabelTopicMapper;

    /**
     * 分页查询标签列表
     *
     * @param topicLabelListDto
     * @return
     */
    public Map<String, Object> labelList(TopicLabelListDto topicLabelListDto) {
        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        // 获取当前用户登录id
        Long currentId = SecurityUtils.getCurrentId();
        log.info("当前用户登录名称和id：{},{}", username, currentId);
        // 设置分页条件
        LambdaQueryWrapper<TopicLabel> topiclabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 判断是否为Hao
        if (currentId != 1L) {
            // 根据当前登录用户查询
            topiclabelLambdaQueryWrapper.like(TopicLabel::getCreateBy, username);
        } else {
            // 是超管
            // 判断是否查询创建人
            if (!StringUtils.isEmpty(topicLabelListDto.getCreateBy())) {
                topiclabelLambdaQueryWrapper.like(TopicLabel::getCreateBy, topicLabelListDto.getCreateBy());
            }
        }
        // 判断标签名称
        if (!StringUtils.isEmpty(topicLabelListDto.getLabelName())) {
            topiclabelLambdaQueryWrapper.like(TopicLabel::getLabelName, topicLabelListDto.getLabelName());
        }
        // 判断创建时间
        if (!StringUtils.isEmpty(topicLabelListDto.getBeginCreateTime()) && !StringUtils.isEmpty(topicLabelListDto.getEndCreateTime())) {
            topiclabelLambdaQueryWrapper.between(TopicLabel::getCreateTime, topicLabelListDto.getBeginCreateTime(), topicLabelListDto.getEndCreateTime());
        }
        topiclabelLambdaQueryWrapper.orderByDesc(TopicLabel::getCreateTime);
        if (topicLabelListDto.getPageNum() == null || topicLabelListDto.getPageSize() == null) {
            List<TopicLabel> topicLabels = topicLabelMapper.selectList(topiclabelLambdaQueryWrapper);
            return Map.of("total", topicLabels.size(), "rows", topicLabels);
        } else {
            // 设置分页参数
            Page<TopicLabel> topicLabelPage = new Page<>(topicLabelListDto.getPageNum(), topicLabelListDto.getPageSize());
            // 开始查询
            Page<TopicLabel> topicLabelList = topicLabelMapper.selectPage(topicLabelPage, topiclabelLambdaQueryWrapper);
            return Map.of("total", topicLabelList.getTotal(), "rows", topicLabelList.getRecords());
        }
    }

    /**
     * 添加题目专题
     *
     * @param topicLabelDto
     */
    public void add(TopicLabelDto topicLabelDto) {
        LambdaQueryWrapper<TopicLabel> topicLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicLabelLambdaQueryWrapper.eq(TopicLabel::getLabelName, topicLabelDto.getLabelName());
        // 查询
        TopicLabel topicLabelDb = topicLabelMapper.selectOne(topicLabelLambdaQueryWrapper);
        if (topicLabelDb != null) {
            throw new TopicException(ResultCodeEnum.LABEL_NAME_EXIST);
        }
        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        TopicLabel topicLabel = new TopicLabel();
        topicLabel.setLabelName(topicLabelDto.getLabelName());
        topicLabel.setCreateBy(username);
        // TODO 异步发送消息给AI审核
        topicLabelMapper.insert(topicLabel);
    }

    /**
     * 修改题目标签
     *
     * @param topicLabelDto
     */
    @Override
    public void update(TopicLabelDto topicLabelDto) {
        // 查询
        TopicLabel topicLabel = topicLabelMapper.selectById(topicLabelDto.getId());
        if (topicLabel == null) {
            throw new TopicException(ResultCodeEnum.CATEGORY_UPDATE_IS_NULL);
        }
        // 开始修改
        topicLabel.setLabelName(topicLabelDto.getLabelName());
        topicLabel.setStatus(StatusEnums.AUDITING.getCode());
        // TODO 异步发送消息给AI审核

        topicLabelMapper.updateById(topicLabel);
    }

    /**
     * 删除题目标签
     *
     * @param ids
     */
    public void delete(Long[] ids) {
        // 校验
        if (ids == null) {
            throw new TopicException(ResultCodeEnum.LABEL_DELETE_IS_NULL);
        }
        for (Long id : ids) {
            LambdaQueryWrapper<TopicLabelTopic> topicLabelTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
            // 查询标签与题目关系表
            topicLabelTopicLambdaQueryWrapper.eq(TopicLabelTopic::getLabelId, id);
            TopicLabelTopic topicLabelTopic = topicLabelTopicMapper.selectOne(topicLabelTopicLambdaQueryWrapper);
            if (topicLabelTopic != null) {
                throw new TopicException(ResultCodeEnum.LABEL_DELETE_TOPIC_ERROR);
            }
            // 删除
            topicLabelMapper.deleteById(id);
        }
    }
}
