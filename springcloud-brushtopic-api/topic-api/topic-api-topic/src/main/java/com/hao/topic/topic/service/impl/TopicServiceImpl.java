package com.hao.topic.topic.service.impl;


import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.topic.api.utils.constant.RabbitConstant;
import com.hao.topic.api.utils.enums.StatusEnums;
import com.hao.topic.api.utils.mq.RabbitService;
import com.hao.topic.common.constant.RedisConstant;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.common.utils.StringUtils;
import com.hao.topic.model.dto.audit.TopicAudit;
import com.hao.topic.model.dto.topic.TopicDto;
import com.hao.topic.model.dto.topic.TopicListDto;
import com.hao.topic.model.dto.topic.TopicRecordCountDto;
import com.hao.topic.model.entity.topic.*;
import com.hao.topic.model.entity.topic.Topic;
import com.hao.topic.model.excel.topic.*;
import com.hao.topic.model.vo.topic.TopicAnswerVo;
import com.hao.topic.model.vo.topic.TopicCollectionVo;
import com.hao.topic.model.vo.topic.TopicDetailVo;
import com.hao.topic.model.vo.topic.TopicVo;
import com.hao.topic.topic.mapper.*;
import com.hao.topic.topic.service.TopicService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/2 17:47
 */
@Service
@Slf4j
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicMapper topicMapper;
    private final TopicSubjectTopicMapper topicSubjectTopicMapper;
    private final TopicSubjectMapper topicSubjectMapper;
    private final TopicLabelMapper topicLabelMapper;
    private final TopicLabelTopicMapper topicLabelTopicMapper;
    private final RabbitService rabbitService;
    private final StringRedisTemplate stringRedisTemplate;
    private final TopicCollectionMapper topicCollectionMapper;
    private final TopicRecordMapper topicRecordMapper;

    /**
     * 查询题目列表
     *
     * @param topicListDto
     * @return
     */
    public Map<String, Object> topicList(TopicListDto topicListDto) {
        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        // 获取当前用户登录id
        Long currentId = SecurityUtils.getCurrentId();
        log.info("当前用户登录名称和id：{},{}", username, currentId);
        // 设置分页条件
        LambdaQueryWrapper<Topic> topicLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 判断是否为Hao
        if (currentId != 1L) {
            // 根据当前登录用户查询
            topicLambdaQueryWrapper.like(Topic::getCreateBy, username);
        } else {
            // 是超管
            // 判断是否查询创建人
            if (!StringUtils.isEmpty(topicListDto.getCreateBy())) {
                topicLambdaQueryWrapper.like(Topic::getCreateBy, topicListDto.getCreateBy());
            }
        }
        // 判断题目名称
        if (!StringUtils.isEmpty(topicListDto.getTopicName())) {
            topicLambdaQueryWrapper.like(Topic::getTopicName, topicListDto.getTopicName());
        }
        // 判断创建时间
        if (!StringUtils.isEmpty(topicListDto.getBeginCreateTime()) && !StringUtils.isEmpty(topicListDto.getEndCreateTime())) {
            topicLambdaQueryWrapper.between(Topic::getCreateTime, topicListDto.getBeginCreateTime(), topicListDto.getEndCreateTime());
        }
        topicLambdaQueryWrapper.orderByDesc(Topic::getCreateTime);
        if (topicListDto.getPageNum() == null || topicListDto.getPageSize() == null) {
            List<Topic> topics = topicMapper.selectList(topicLambdaQueryWrapper);
            List<TopicVo> list = topics.stream().map(topic -> {
                TopicVo topicVo = new TopicVo();
                BeanUtils.copyProperties(topic, topicVo);
                // 根据专题id查询专题
                LambdaQueryWrapper<TopicSubjectTopic> topicSubjectTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicSubjectTopicLambdaQueryWrapper.eq(TopicSubjectTopic::getTopicId, topic.getId());
                TopicSubjectTopic topicSubjectTopic = topicSubjectTopicMapper.selectOne(topicSubjectTopicLambdaQueryWrapper);
                if (topicSubjectTopic != null) {
                    TopicSubject topicSubject = topicSubjectMapper.selectById(topicSubjectTopic.getSubjectId());
                    if (topicSubject != null) {
                        topicVo.setSubject(topicSubject.getSubjectName());
                    }
                }
                List<String> stringList = new ArrayList<>();
                // 封装标签
                LambdaQueryWrapper<TopicLabelTopic> topicLabelTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicLabelTopicLambdaQueryWrapper.eq(TopicLabelTopic::getTopicId, topic.getId());
                List<TopicLabelTopic> topicLabelTopics = topicLabelTopicMapper.selectList(topicLabelTopicLambdaQueryWrapper);
                if (CollectionUtils.isNotEmpty(topicLabelTopics)) {
                    topicLabelTopics.forEach(topicLabelTopic -> {
                        TopicLabel topicLabel = topicLabelMapper.selectById(topicLabelTopic.getLabelId());
                        if (topicLabel != null) {
                            stringList.add(topicLabel.getLabelName());
                        }
                    });
                }
                topicVo.setLabels(stringList);
                return topicVo;
            }).toList();
            // 查询标签
            return Map.of("total", list.size(), "rows", list);
        } else {
            // 设置分页参数
            Page<Topic> topicPage = new Page<>(topicListDto.getPageNum(), topicListDto.getPageSize());
            // 开始查询
            Page<Topic> topicListPage = topicMapper.selectPage(topicPage, topicLambdaQueryWrapper);
            List<TopicVo> list = topicListPage.getRecords().stream().map(topic -> {
                TopicVo topicVo = new TopicVo();
                BeanUtils.copyProperties(topic, topicVo);
                // 根据专题id查询专题
                LambdaQueryWrapper<TopicSubjectTopic> topicSubjectTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicSubjectTopicLambdaQueryWrapper.eq(TopicSubjectTopic::getTopicId, topic.getId());
                TopicSubjectTopic topicSubjectTopic = topicSubjectTopicMapper.selectOne(topicSubjectTopicLambdaQueryWrapper);
                if (topicSubjectTopic != null) {
                    TopicSubject topicSubject = topicSubjectMapper.selectById(topicSubjectTopic.getSubjectId());
                    if (topicSubject != null) {
                        topicVo.setSubject(topicSubject.getSubjectName());
                    }
                }
                List<String> stringList = new ArrayList<>();
                // 封装标签
                LambdaQueryWrapper<TopicLabelTopic> topicLabelTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicLabelTopicLambdaQueryWrapper.eq(TopicLabelTopic::getTopicId, topic.getId());
                List<TopicLabelTopic> topicLabelTopics = topicLabelTopicMapper.selectList(topicLabelTopicLambdaQueryWrapper);
                if (CollectionUtils.isNotEmpty(topicLabelTopics)) {
                    topicLabelTopics.forEach(topicLabelTopic -> {
                        TopicLabel topicLabel = topicLabelMapper.selectById(topicLabelTopic.getLabelId());
                        if (topicLabel != null) {
                            stringList.add(topicLabel.getLabelName());
                        }
                    });
                }
                topicVo.setLabels(stringList);
                return topicVo;
            }).toList();
            return Map.of("total", topicListPage.getTotal(), "rows", list);
        }
    }

    /**
     * 新增题目
     *
     * @param topicDto
     */
    @Transactional
    public void add(TopicDto topicDto) {
        LambdaQueryWrapper<Topic> topicLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicLambdaQueryWrapper.eq(Topic::getTopicName, topicDto.getTopicName());
        // 查询
        Topic topicDb = topicMapper.selectOne(topicLambdaQueryWrapper);
        if (topicDb != null) {
            throw new TopicException(ResultCodeEnum.TOPIC_NAME_EXIST);
        }
        // 查询专题
        TopicSubject topicSubject = topicSubjectMapper.selectById(topicDto.getSubjectId());
        // 判断
        if (topicSubject == null) {
            throw new TopicException(ResultCodeEnum.SUBJECT_NOT_EXIST);
        }
        StringBuilder labelNames = new StringBuilder();
        // 查询标签
        List<TopicLabel> topicLabels = topicLabelMapper.selectBatchIds(topicDto.getLabelIds());
        if (CollectionUtils.isEmpty(topicLabels)) {
            throw new TopicException(ResultCodeEnum.LABEL_NOT_EXIST);
        }
        for (TopicLabel topicLabel : topicLabels) {
            labelNames.append(topicLabel.getLabelName());
            // 拼接最后一个不要拼接
            if (topicLabels.size() != topicLabels.indexOf(topicLabel) + 1) {
                labelNames.append(":");
            }
        }
        log.info("标签名称：{}", labelNames);


        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        // 新增题目
        Topic topic = new Topic();
        BeanUtils.copyProperties(topicDto, topic);
        // 设置创建人
        topic.setCreateBy(username);
        // 每日题目只能有9题
        if (topic.getIsEveryday() == 1) {
            LambdaQueryWrapper<Topic> topicLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            topicLambdaQueryWrapper1.eq(Topic::getIsEveryday, 1);
            List<Topic> topics = topicMapper.selectList(topicLambdaQueryWrapper1);
            if (CollectionUtils.isNotEmpty(topics) && topics.size() >= 9) {
                throw new TopicException(ResultCodeEnum.TOPIC_EVERYDAY_ERROR);
            }
        }

        // 获取当前用户id
        Long currentId = SecurityUtils.getCurrentId();
        if (currentId == 1L) {
            // 是开发者不需要审核
            topic.setStatus(StatusEnums.NORMAL.getCode());
            // 开始插入
            topicMapper.insert(topic);
        } else {
            topicMapper.insert(topic);
            // 不是开发者需要审核
            topic.setStatus(StatusEnums.AUDITING.getCode());
            // 异步发送信息给AI审核
            TopicAudit topicAudit = new TopicAudit();
            topicAudit.setId(topic.getId());
            topicAudit.setTopicName(topic.getTopicName());
            topicAudit.setAccount(username);
            topicAudit.setAnswer(topic.getAnswer());
            topicAudit.setUserId(currentId);
            topicAudit.setTopicSubjectName(topicSubject.getSubjectName());
            topicAudit.setTopicLabelName(labelNames.toString());
            String topicAuditJson = JSON.toJSONString(topicAudit);
            log.info("发送消息{}", topicAuditJson);
            rabbitService.sendMessage(RabbitConstant.TOPIC_AUDIT_EXCHANGE, RabbitConstant.TOPIC_AUDIT_ROUTING_KEY_NAME, topicAuditJson);
        }

        // 插入到专题题目关系表中
        TopicSubjectTopic topicSubjectTopic = new TopicSubjectTopic();
        topicSubjectTopic.setTopicId(topic.getId());
        topicSubjectTopic.setSubjectId(topicDto.getSubjectId());
        topicSubjectTopicMapper.insert(topicSubjectTopic);

        // 更新专题数量
        topicSubject.setTopicCount(topicSubject.getTopicCount() + 1);
        topicSubjectMapper.updateById(topicSubject);
        for (TopicLabel topicLabel : topicLabels) {
            // 插入到题目标签关系表中
            TopicLabelTopic topicLabelTopic = new TopicLabelTopic();
            topicLabelTopic.setTopicId(topic.getId());
            topicLabelTopic.setLabelId(topicLabel.getId());
            topicLabelTopicMapper.insert(topicLabelTopic);

            // 更新标签被使用次数
            topicLabel.setUseCount(topicLabel.getUseCount() + 1);
            topicLabelMapper.updateById(topicLabel);
        }
    }

    /**
     * 修改题目
     *
     * @param topicDto
     */
    @Transactional
    public void update(TopicDto topicDto) {
        // 根据id查询
        Topic oldTopic = topicMapper.selectById(topicDto.getId());
        if (oldTopic == null) {
            throw new TopicException(ResultCodeEnum.TOPIC_UPDATE_IS_NULL);
        }
        // 查询专题
        TopicSubject topicSubject = topicSubjectMapper.selectById(topicDto.getSubjectId());
        // 判断
        if (topicSubject == null) {
            throw new TopicException(ResultCodeEnum.SUBJECT_NOT_EXIST);
        }
        // 查询标签
        List<TopicLabel> topicLabels = topicLabelMapper.selectBatchIds(topicDto.getLabelIds());
        if (CollectionUtils.isEmpty(topicLabels)) {
            throw new TopicException(ResultCodeEnum.LABEL_NOT_EXIST);
        }
        StringBuilder labelNames = new StringBuilder();
        for (TopicLabel topicLabel : topicLabels) {
            labelNames.append(topicLabel.getLabelName());
            // 拼接最后一个不要拼接
            if (topicLabels.size() != topicLabels.indexOf(topicLabel) + 1) {
                labelNames.append(":");
            }
        }
        // 修改题目
        Topic topic = new Topic();
        BeanUtils.copyProperties(topicDto, topic);
        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        // 每日题目只能有9题
        if (topic.getIsEveryday() == 1) {
            LambdaQueryWrapper<Topic> topicLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            topicLambdaQueryWrapper1.eq(Topic::getIsEveryday, 1);
            List<Topic> topics = topicMapper.selectList(topicLambdaQueryWrapper1);
            if (CollectionUtils.isNotEmpty(topics) && topics.size() >= 9) {
                throw new TopicException(ResultCodeEnum.TOPIC_EVERYDAY_ERROR);
            }
        }

        // 获取当前用户id
        Long currentId = SecurityUtils.getCurrentId();
        if (currentId == 1L) {
            // 是开发者不需要审核
            topic.setStatus(StatusEnums.NORMAL.getCode());
        } else {
            // 不是开发者需要审核
            topic.setStatus(StatusEnums.AUDITING.getCode());
            // 异步发送信息给AI审核
            TopicAudit topicAudit = new TopicAudit();
            topicAudit.setId(topicDto.getId());
            topicAudit.setTopicName(topicDto.getTopicName());
            topicAudit.setAccount(username);
            topicAudit.setAnswer(topicDto.getAnswer());
            topicAudit.setUserId(currentId);
            topicAudit.setTopicSubjectName(topicSubject.getSubjectName());
            topicAudit.setTopicLabelName(labelNames.toString());
            String topicAuditJson = JSON.toJSONString(topicAudit);
            log.info("发送消息{}", topicAuditJson);
            rabbitService.sendMessage(RabbitConstant.TOPIC_AUDIT_EXCHANGE, RabbitConstant.TOPIC_AUDIT_ROUTING_KEY_NAME, topicAuditJson);
        }
        topic.setFailMsg("");
        // 开始更新
        topicMapper.updateById(topic);

        // 查询专题题目关系表
        LambdaQueryWrapper<TopicSubjectTopic> topicSubjectTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicSubjectTopicLambdaQueryWrapper.eq(TopicSubjectTopic::getTopicId, oldTopic.getId());
        TopicSubjectTopic topicSubjectTopicDb = topicSubjectTopicMapper.selectOne(topicSubjectTopicLambdaQueryWrapper);
        if (topicSubjectTopicDb != null) {
            // 查询专题
            TopicSubject topicSubjectDb = topicSubjectMapper.selectById(topicSubjectTopicDb.getSubjectId());
            if (topicSubjectDb != null) {
                // 更新专题数量
                topicSubjectDb.setTopicCount(topicSubjectDb.getTopicCount() - 1);
                topicSubjectMapper.updateById(topicSubjectDb);
            }
            // 删除
            topicSubjectTopicMapper.deleteById(topicSubjectTopicDb);
        }

        // 插入到专题题目关系表中
        TopicSubjectTopic topicSubjectTopic = new TopicSubjectTopic();
        topicSubjectTopic.setTopicId(topic.getId());
        topicSubjectTopic.setSubjectId(topicDto.getSubjectId());
        topicSubjectTopicMapper.insert(topicSubjectTopic);
        // 更新次数
        topicSubject.setTopicCount(topicSubject.getTopicCount() + 1);
        topicSubjectMapper.updateById(topicSubject);

        // 查询标签题目关系表
        LambdaQueryWrapper<TopicLabelTopic> topicLabelTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicLabelTopicLambdaQueryWrapper.eq(TopicLabelTopic::getTopicId, oldTopic.getId());
        List<TopicLabelTopic> topicLabelTopics = topicLabelTopicMapper.selectList(topicLabelTopicLambdaQueryWrapper);
        if (!CollectionUtils.isEmpty(topicLabelTopics)) {
            for (TopicLabelTopic topicLabelTopic : topicLabelTopics) {
                // 查询标签
                TopicLabel topicLabelDb = topicLabelMapper.selectById(topicLabelTopic.getLabelId());
                if (topicLabelDb != null) {
                    // 更新标签被使用次数
                    topicLabelDb.setUseCount(topicLabelDb.getUseCount() - 1);
                    topicLabelMapper.updateById(topicLabelDb);
                }
                // 删除
                topicLabelTopicMapper.deleteById(topicLabelTopic);
            }
        }

        for (TopicLabel topicLabel : topicLabels) {
            // 插入到题目标签关系表中
            TopicLabelTopic topicLabelTopic = new TopicLabelTopic();
            topicLabelTopic.setTopicId(topic.getId());
            topicLabelTopic.setLabelId(topicLabel.getId());
            topicLabelTopicMapper.insert(topicLabelTopic);

            // 更新标签被使用次数
            topicLabel.setUseCount(topicLabel.getUseCount() + 1);
            topicLabelMapper.updateById(topicLabel);
        }
    }


    /**
     * 删除题目
     *
     * @param ids
     */
    @Transactional
    public void delete(Long[] ids) {
        // 校验
        if (ids == null) {
            throw new TopicException(ResultCodeEnum.TOPIC_DELETE_IS_NULL);
        }
        // 遍历
        for (Long id : ids) {
            Topic topic = topicMapper.selectById(id);
            if (topic == null) {
                throw new TopicException(ResultCodeEnum.TOPIC_DELETE_IS_NULL);
            }
            // 删除题目表
            topicMapper.deleteById(id);
            // 查询题目专题关系表
            LambdaQueryWrapper<TopicSubjectTopic> topicSubjectTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
            topicSubjectTopicLambdaQueryWrapper.eq(TopicSubjectTopic::getTopicId, id);
            TopicSubjectTopic topicSubjectTopic = topicSubjectTopicMapper.selectOne(topicSubjectTopicLambdaQueryWrapper);
            if (topicSubjectTopic != null) {
                // 查询专题
                TopicSubject topicSubjectDb = topicSubjectMapper.selectById(topicSubjectTopic.getSubjectId());
                if (topicSubjectDb != null) {
                    // 更新专题数量
                    topicSubjectDb.setTopicCount(topicSubjectDb.getTopicCount() - 1);
                    topicSubjectMapper.updateById(topicSubjectDb);
                }
            }
            // 删除题目专题关系表
            topicSubjectTopicMapper.deleteById(topicSubjectTopic);

            // 查询题目标签关系表
            LambdaQueryWrapper<TopicLabelTopic> topicLabelTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
            topicLabelTopicLambdaQueryWrapper.eq(TopicLabelTopic::getTopicId, id);
            List<TopicLabelTopic> topicLabelTopics = topicLabelTopicMapper.selectList(topicLabelTopicLambdaQueryWrapper);
            if (!CollectionUtils.isEmpty(topicLabelTopics)) {
                for (TopicLabelTopic topicLabelTopic : topicLabelTopics) {
                    // 查询标签
                    TopicLabel topicLabelDb = topicLabelMapper.selectById(topicLabelTopic.getLabelId());
                    if (topicLabelDb != null) {
                        // 更新标签被使用次数
                        topicLabelDb.setUseCount(topicLabelDb.getUseCount() - 1);
                        topicLabelMapper.updateById(topicLabelDb);
                    }
                }
            }
            // 删除题目标签关系表
            topicLabelTopicMapper.delete(topicLabelTopicLambdaQueryWrapper);
        }
    }

    /**
     * 获取导出数据
     *
     * @param topicListDto
     * @param ids
     * @return
     */
    public List<TopicExcelExport> getExcelVo(TopicListDto topicListDto, Long[] ids) {
        // 是否有id
        if (ids[0] != 0) {
            // 根据id查询
            List<Topic> topics = topicMapper.selectBatchIds(Arrays.asList(ids));
            if (CollectionUtils.isEmpty(topics)) {
                throw new TopicException(ResultCodeEnum.EXPORT_ERROR);
            }
            return topics.stream().map(topic -> {
                TopicExcelExport topicExcelExport = new TopicExcelExport();
                BeanUtils.copyProperties(topic, topicExcelExport);
                // 状态特殊处理
                topicExcelExport.setStatus(StatusEnums.getMessageByCode(topic.getStatus()));
                // 处理专题
                // 查询专题题目关系表
                LambdaQueryWrapper<TopicSubjectTopic> topicSubjectTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicSubjectTopicLambdaQueryWrapper.eq(TopicSubjectTopic::getTopicId, topic.getId());
                TopicSubjectTopic topicSubjectTopic = topicSubjectTopicMapper.selectOne(topicSubjectTopicLambdaQueryWrapper);
                if (topicSubjectTopic != null) {
                    // 查询专题
                    TopicSubject topicSubjectDb = topicSubjectMapper.selectById(topicSubjectTopic.getSubjectId());
                    if (topicSubjectDb != null) {
                        topicExcelExport.setSubjectName(topicSubjectDb.getSubjectName());
                    }
                }
                // 处理标签
                // 查询标签题目关系表
                LambdaQueryWrapper<TopicLabelTopic> topicLabelTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicLabelTopicLambdaQueryWrapper.eq(TopicLabelTopic::getTopicId, topic.getId());
                List<TopicLabelTopic> topicLabelTopics = topicLabelTopicMapper.selectList(topicLabelTopicLambdaQueryWrapper);
                StringBuilder labelNames = new StringBuilder();
                if (!CollectionUtils.isEmpty(topicLabelTopics)) {
                    for (TopicLabelTopic topicLabelTopic : topicLabelTopics) {
                        // 查询标签
                        TopicLabel topicLabelDb = topicLabelMapper.selectById(topicLabelTopic.getLabelId());
                        if (topicLabelDb != null) {
                            labelNames.append(topicLabelDb.getLabelName());
                            // 拼接最后一个不要拼接
                            if (topicLabelTopics.size() != topicLabelTopics.indexOf(topicLabelTopic) + 1) {
                                labelNames.append(":");
                            }
                        }
                    }
                }
                topicExcelExport.setLabelName(labelNames.toString());
                return topicExcelExport;
            }).collect(Collectors.toList());
        } else {
            Map<String, Object> map = topicList(topicListDto);
            if (map.get("rows") == null) {
                throw new TopicException(ResultCodeEnum.EXPORT_ERROR);
            }
            List<Topic> topics = (List<Topic>) map.get("rows");
            // 封装返回数据
            return topics.stream().map(topic -> {
                TopicExcelExport topicExcelExport = new TopicExcelExport();
                BeanUtils.copyProperties(topic, topicExcelExport);
                // 状态特殊处理
                topicExcelExport.setStatus(StatusEnums.getMessageByCode(topic.getStatus()));
                return topicExcelExport;
            }).collect(Collectors.toList());
        }
    }

    /**
     * 会员导入
     *
     * @param multipartFile
     * @param updateSupport
     * @return
     */
    public String memberImport(MultipartFile multipartFile, Boolean updateSupport) throws IOException {
        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        // 读取数据
        List<TopicMemberExcel> excelVoList = EasyExcel.read(multipartFile.getInputStream())
                // 映射数据
                .head(TopicMemberExcel.class)
                // 读取工作表
                .sheet()
                // 读取并同步返回数据
                .doReadSync();
        // 校验
        if (CollectionUtils.isEmpty(excelVoList)) {
            throw new TopicException(ResultCodeEnum.IMPORT_ERROR);
        }
        // 计算成功的数量
        int successNum = 0;
        // 计算失败的数量
        int failureNum = 0;
        // 拼接成功消息
        StringBuilder successMsg = new StringBuilder();
        // 拼接错误消息
        StringBuilder failureMsg = new StringBuilder();
        Long currentId = SecurityUtils.getCurrentId();

        // 遍历
        for (TopicMemberExcel topicExcel : excelVoList) {
            try {
                // 查询这个题目是否存在
                LambdaQueryWrapper<Topic> topicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicLambdaQueryWrapper.eq(Topic::getTopicName, topicExcel.getTopicName());
                Topic topic = topicMapper.selectOne(topicLambdaQueryWrapper);
                if (StringUtils.isNull(topic)) {
                    // 不存在插入
                    Topic topicDb = new Topic();
                    BeanUtils.copyProperties(topicExcel, topicDb);
                    topicDb.setCreateBy(username);
                    if (topicExcel.getSubjectName() == null) {
                        throw new TopicException(ResultCodeEnum.TOPIC_SUBJECT_IS_NULL);
                    }
                    if (topicExcel.getLabelName() == null) {
                        throw new TopicException(ResultCodeEnum.TOPIC_LABEL_IS_NULL);
                    }
                    // 查询专题
                    LambdaQueryWrapper<TopicSubject> topicSubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    topicSubjectLambdaQueryWrapper.eq(TopicSubject::getSubjectName, topicExcel.getSubjectName());
                    TopicSubject topicSubjectDb = null;
                    if (topicSubjectMapper != null) {
                        topicSubjectDb = topicSubjectMapper.selectOne(topicSubjectLambdaQueryWrapper);
                    }
                    if (StringUtils.isNull(topicSubjectDb)) {
                        throw new TopicException(ResultCodeEnum.TOPIC_SUBJECT_IS_NULL);
                    }
                    // 判断是否是开发者
                    if (currentId == 1L) {
                        // 是开发者不需要审核
                        topicDb.setStatus(StatusEnums.NORMAL.getCode());
                        topicMapper.insert(topicDb);
                    } else {
                        // 不是开发者进行审核
                        topicDb.setStatus(StatusEnums.AUDITING.getCode());
                        topicMapper.insert(topicDb);
                    }

                    topicSubjectDb.setTopicCount(topicSubjectDb.getTopicCount() + 1);
                    topicSubjectMapper.updateById(topicSubjectDb);
                    // 添加到题目关联专题表中
                    TopicSubjectTopic topicSubject = new TopicSubjectTopic();
                    topicSubject.setTopicId(topicDb.getId());
                    topicSubject.setSubjectId(topicSubjectDb.getId());
                    topicSubjectTopicMapper.insert(topicSubject);

                    // 将标签分割 标签1:标签2:标签3
                    String[] labelNames = topicExcel.getLabelName().split(":");
                    // 校验labelNames是否存在相同的标签
                    for (int i = 0; i < labelNames.length; i++) {
                        for (int j = i + 1; j < labelNames.length; j++) {
                            if (labelNames[i].equals(labelNames[j])) {
                                throw new TopicException(ResultCodeEnum.TOPIC_LABEL_IS_NULL);
                            }
                        }
                    }
                    for (String labelName : labelNames) {
                        if (StringUtils.isNull(labelName)) {
                            throw new TopicException(ResultCodeEnum.TOPIC_LABEL_IS_NULL);
                        }
                        // 根据标签名称查询标签
                        LambdaQueryWrapper<TopicLabel> topicLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        topicLabelLambdaQueryWrapper.eq(TopicLabel::getLabelName, labelName);
                        TopicLabel topicLabelDb = topicLabelMapper.selectOne(topicLabelLambdaQueryWrapper);
                        if (topicLabelDb == null) {
                            throw new TopicException(ResultCodeEnum.TOPIC_LABEL_IS_NULL);
                        }
                        // 有标签修改
                        topicLabelDb.setUseCount(topicLabelDb.getUseCount() + 1);
                        topicLabelMapper.updateById(topicLabelDb);

                        // 添加到题目标签关系表中
                        TopicLabelTopic topicLabelTopic = new TopicLabelTopic();
                        topicLabelTopic.setTopicId(topicDb.getId());
                        topicLabelTopic.setLabelId(topicLabelDb.getId());
                        topicLabelTopicMapper.insert(topicLabelTopic);
                    }
                    // 判断是否是开发者
                    if (currentId == 1L) {
                    } else {
                        // 异步发送消息给AI审核
                        TopicAudit topicAudit = new TopicAudit();
                        topicAudit.setTopicName(topicExcel.getTopicName());
                        topicAudit.setAnswer(topicExcel.getAnswer());
                        topicAudit.setAccount(username);
                        topicAudit.setUserId(currentId);
                        topicAudit.setTopicSubjectName(topicExcel.getSubjectName());
                        topicAudit.setTopicLabelName(topicExcel.getLabelName());
                        topicAudit.setId(topicDb.getId());
                        // 转换json
                        String topicAuditJson = JSON.toJSONString(topicAudit);
                        rabbitService.sendMessage(RabbitConstant.TOPIC_AUDIT_EXCHANGE, RabbitConstant.TOPIC_AUDIT_ROUTING_KEY_NAME, topicAuditJson);
                    }
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("-题目：").append(topicDb.getTopicName()).append("-导入成功");
                } else if (updateSupport) {
                    if (topicExcel.getSubjectName() == null) {
                        throw new TopicException(ResultCodeEnum.TOPIC_SUBJECT_IS_NULL);
                    }
                    if (topicExcel.getLabelName() == null) {
                        throw new TopicException(ResultCodeEnum.TOPIC_LABEL_IS_NULL);
                    }
                    // 将标签分割 标签1:标签2:标签3
                    String[] labelNames = topicExcel.getLabelName().split(":");
                    // 校验labelNames是否存在相同的标签
                    for (int i = 0; i < labelNames.length; i++) {
                        for (int j = i + 1; j < labelNames.length; j++) {
                            if (labelNames[i].equals(labelNames[j])) {
                                throw new TopicException(ResultCodeEnum.TOPIC_LABEL_IS_NULL);
                            }
                        }
                    }
                    // 查询专题题目关系表
                    LambdaQueryWrapper<TopicSubjectTopic> topicSubjectTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    topicSubjectTopicLambdaQueryWrapper.eq(TopicSubjectTopic::getTopicId, topic.getId());
                    TopicSubjectTopic topicSubjectTopic = topicSubjectTopicMapper.selectOne(topicSubjectTopicLambdaQueryWrapper);
                    if (topicSubjectTopic != null) {
                        // 查询专题
                        TopicSubject topicSubject = topicSubjectMapper.selectById(topicSubjectTopic.getSubjectId());
                        if (topicSubject != null) {
                            // 判断数据库的专题和当前要修改的专题是否一致
                            if (!topicSubject.getSubjectName().equals(topicExcel.getSubjectName())) {
                                // 不一致更新当前专题被使用次数-1
                                topicSubject.setTopicCount(topicSubject.getTopicCount() - 1);
                                topicSubjectMapper.updateById(topicSubject);
                                // 然后查询当前要添加的专题
                                LambdaQueryWrapper<TopicSubject> topicSubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
                                topicSubjectLambdaQueryWrapper.eq(TopicSubject::getSubjectName, topicExcel.getSubjectName());
                                TopicSubject topicSubjectDb = topicSubjectMapper.selectOne(topicSubjectLambdaQueryWrapper);
                                if (topicSubjectDb != null) {
                                    topicSubjectDb.setTopicCount(topicSubjectDb.getTopicCount() + 1);
                                    topicSubjectMapper.updateById(topicSubjectDb);
                                    // 添加到题目关联专题表中
                                    TopicSubjectTopic topicSubjectTopicDb = new TopicSubjectTopic();
                                    topicSubjectTopicDb.setSubjectId(topicSubjectDb.getId());
                                    topicSubjectTopicDb.setTopicId(topic.getId());
                                    topicSubjectTopicMapper.insert(topicSubjectTopicDb);
                                }
                                // 删除以前的题目专题关联关系
                                topicSubjectTopicMapper.deleteById(topicSubjectTopic);
                            }
                        }
                    }

                    // 查询标签题目关系表
                    LambdaQueryWrapper<TopicLabelTopic> topicLabelTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    topicLabelTopicLambdaQueryWrapper.eq(TopicLabelTopic::getTopicId, topic.getId());
                    List<TopicLabelTopic> topicLabelTopics = topicLabelTopicMapper.selectList(topicLabelTopicLambdaQueryWrapper);
                    // 校验一下
                    if (CollectionUtils.isNotEmpty(topicLabelTopics)) {
                        // 获取所有的标签id
                        List<Long> labelIds = topicLabelTopics.stream()
                                .map(TopicLabelTopic::getLabelId)
                                .toList();
                        // 查询标签
                        List<TopicLabel> topicLabels = topicLabelMapper.selectBatchIds(labelIds);
                        // 更新所有标签次数-1
                        topicLabels.forEach(topicLabel -> {
                            topicLabel.setUseCount(topicLabel.getUseCount() - 1);
                            topicLabelMapper.updateById(topicLabel);
                        });
                        // 先删除题目关系表
                        topicLabelTopicMapper.delete(topicLabelTopicLambdaQueryWrapper);


                        // 校验要修改的标签名称是否与以前的名称是否一样
                        for (String labelName : labelNames) {
                            // 然后查询当前要添加的标签
                            LambdaQueryWrapper<TopicLabel> topicLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
                            topicLabelLambdaQueryWrapper.eq(TopicLabel::getLabelName, labelName);
                            TopicLabel topicLabelDb = topicLabelMapper.selectOne(topicLabelLambdaQueryWrapper);
                            if (topicLabelDb != null) {
                                topicLabelDb.setUseCount(topicLabelDb.getUseCount() + 1);
                                topicLabelMapper.updateById(topicLabelDb);
                                // 添加到题目关联标签表中
                                TopicLabelTopic topicLabelTopicDb = new TopicLabelTopic();
                                topicLabelTopicDb.setLabelId(topicLabelDb.getId());
                                topicLabelTopicDb.setTopicId(topic.getId());
                                topicLabelTopicMapper.insert(topicLabelTopicDb);
                            }
                        }
                    }


                    // 判断是否是开发者
                    if (currentId == 1L) {
                        // 是开发者不需要审核
                        topic.setStatus(StatusEnums.NORMAL.getCode());
                    } else {
                        // 不是开发者进行审核
                        topic.setStatus(StatusEnums.AUDITING.getCode());
                        // 异步发送消息给AI审核
                        TopicAudit topicAudit = new TopicAudit();
                        topicAudit.setTopicName(topicExcel.getTopicName());
                        topicAudit.setAnswer(topicExcel.getAnswer());
                        topicAudit.setAccount(username);
                        topicAudit.setUserId(currentId);
                        topicAudit.setTopicSubjectName(topicExcel.getSubjectName());
                        topicAudit.setTopicLabelName(topicExcel.getLabelName());
                        topicAudit.setId(topic.getId());
                        // 转换json
                        String topicAuditJson = JSON.toJSONString(topicAudit);
                        rabbitService.sendMessage(RabbitConstant.TOPIC_AUDIT_EXCHANGE, RabbitConstant.TOPIC_AUDIT_ROUTING_KEY_NAME, topicAuditJson);
                    }
                    topic.setFailMsg("");
                    // 更新
                    BeanUtils.copyProperties(topicExcel, topic);
                    topicMapper.updateById(topic);

                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("-题目：").append(topic.getTopicName()).append("-更新成功");


                } else {
                    // 已存在
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("-题目：").append(topic.getTopicName()).append("-已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "-题目： " + topicExcel.getTopicName() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new TopicException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 管理员导入
     *
     * @param multipartFile
     * @param updateSupport
     * @return
     */
    public String adminImport(MultipartFile multipartFile, Boolean updateSupport) throws IOException {
        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        // 读取数据
        List<TopicExcel> excelVoList = EasyExcel.read(multipartFile.getInputStream())
                // 映射数据
                .head(TopicExcel.class)
                // 读取工作表
                .sheet()
                // 读取并同步返回数据
                .doReadSync();
        // 校验
        if (CollectionUtils.isEmpty(excelVoList)) {
            throw new TopicException(ResultCodeEnum.IMPORT_ERROR);
        }
        // 计算成功的数量
        int successNum = 0;
        // 计算失败的数量
        int failureNum = 0;
        // 拼接成功消息
        StringBuilder successMsg = new StringBuilder();
        // 拼接错误消息
        StringBuilder failureMsg = new StringBuilder();
        Long currentId = SecurityUtils.getCurrentId();
        // 遍历
        for (TopicExcel topicExcel : excelVoList) {
            try {

                // 查询这个题目是否存在
                LambdaQueryWrapper<Topic> topicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicLambdaQueryWrapper.eq(Topic::getTopicName, topicExcel.getTopicName());
                Topic topic = topicMapper.selectOne(topicLambdaQueryWrapper);
                if (StringUtils.isNull(topic)) {
                    // 不存在插入
                    Topic topicDb = new Topic();
                    BeanUtils.copyProperties(topicExcel, topicDb);
                    topicDb.setCreateBy(username);
                    if (topicExcel.getSubjectName() == null) {
                        throw new TopicException(ResultCodeEnum.TOPIC_SUBJECT_IS_NULL);
                    }
                    if (topicExcel.getLabelName() == null) {
                        throw new TopicException(ResultCodeEnum.TOPIC_LABEL_IS_NULL);
                    }
                    // 查询专题
                    LambdaQueryWrapper<TopicSubject> topicSubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    topicSubjectLambdaQueryWrapper.eq(TopicSubject::getSubjectName, topicExcel.getSubjectName());
                    TopicSubject topicSubjectDb = topicSubjectMapper.selectOne(topicSubjectLambdaQueryWrapper);
                    if (StringUtils.isNull(topicSubjectDb)) {
                        throw new TopicException(ResultCodeEnum.TOPIC_SUBJECT_IS_NULL);
                    }
                    topicSubjectDb.setTopicCount(topicSubjectDb.getTopicCount() + 1);
                    topicSubjectMapper.updateById(topicSubjectDb);
                    // 添加到题目关联专题表中
                    TopicSubjectTopic topicSubject = new TopicSubjectTopic();
                    topicSubject.setTopicId(topicDb.getId());
                    topicSubject.setSubjectId(topicSubjectDb.getId());
                    topicSubjectTopicMapper.insert(topicSubject);

                    // 将标签分割 标签1:标签2:标签3
                    String[] labelNames = topicExcel.getLabelName().split(":");
                    // 校验labelNames是否存在相同的标签
                    for (int i = 0; i < labelNames.length; i++) {
                        for (int j = i + 1; j < labelNames.length; j++) {
                            if (labelNames[i].equals(labelNames[j])) {
                                throw new TopicException(ResultCodeEnum.TOPIC_LABEL_IS_NULL);
                            }
                        }
                    }
                    for (String labelName : labelNames) {
                        if (StringUtils.isNull(labelName)) {
                            throw new TopicException(ResultCodeEnum.TOPIC_LABEL_IS_NULL);
                        }
                        // 根据标签名称查询标签
                        LambdaQueryWrapper<TopicLabel> topicLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        topicLabelLambdaQueryWrapper.eq(TopicLabel::getLabelName, labelName);
                        TopicLabel topicLabelDb = topicLabelMapper.selectOne(topicLabelLambdaQueryWrapper);
                        if (topicLabelDb == null) {
                            throw new TopicException(ResultCodeEnum.TOPIC_LABEL_IS_NULL);
                        }
                        // 有标签修改
                        topicLabelDb.setUseCount(topicLabelDb.getUseCount() + 1);
                        topicLabelMapper.updateById(topicLabelDb);

                        // 添加到题目标签关系表中
                        TopicLabelTopic topicLabelTopic = new TopicLabelTopic();
                        topicLabelTopic.setTopicId(topicDb.getId());
                        topicLabelTopic.setLabelId(topicLabelDb.getId());
                        topicLabelTopicMapper.insert(topicLabelTopic);
                    }
                    // 判断是否是开发者
                    if (currentId == 1L) {
                        // 是开发者不需要审核
                        topicDb.setStatus(StatusEnums.NORMAL.getCode());
                        topicMapper.insert(topicDb);
                    } else {
                        // 不是开发者进行审核
                        topicDb.setStatus(StatusEnums.AUDITING.getCode());
                        topicMapper.insert(topicDb);
                        // 异步发送消息给AI审核
                        TopicAudit topicAudit = new TopicAudit();
                        topicAudit.setTopicName(topicExcel.getTopicName());
                        topicAudit.setAnswer(topicExcel.getAnswer());
                        topicAudit.setAccount(username);
                        topicAudit.setUserId(currentId);
                        topicAudit.setTopicSubjectName(topicExcel.getSubjectName());
                        topicAudit.setTopicLabelName(topicExcel.getLabelName());
                        topicAudit.setId(topicDb.getId());
                        // 转换json
                        String topicAuditJson = JSON.toJSONString(topicAudit);
                        rabbitService.sendMessage(RabbitConstant.TOPIC_AUDIT_EXCHANGE, RabbitConstant.TOPIC_AUDIT_ROUTING_KEY_NAME, topicAuditJson);
                    }
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("-题目：").append(topicDb.getTopicName()).append("-导入成功");

                } else if (updateSupport) {
                    if (topicExcel.getSubjectName() == null) {
                        throw new TopicException(ResultCodeEnum.TOPIC_SUBJECT_IS_NULL);
                    }
                    if (topicExcel.getLabelName() == null) {
                        throw new TopicException(ResultCodeEnum.TOPIC_LABEL_IS_NULL);
                    }
                    // 将标签分割 标签1:标签2:标签3
                    String[] labelNames = topicExcel.getLabelName().split(":");
                    // 校验labelNames是否存在相同的标签
                    for (int i = 0; i < labelNames.length; i++) {
                        for (int j = i + 1; j < labelNames.length; j++) {
                            if (labelNames[i].equals(labelNames[j])) {
                                throw new TopicException(ResultCodeEnum.TOPIC_LABEL_IS_NULL);
                            }
                        }
                    }
                    // 查询专题题目关系表
                    LambdaQueryWrapper<TopicSubjectTopic> topicSubjectTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    topicSubjectTopicLambdaQueryWrapper.eq(TopicSubjectTopic::getTopicId, topic.getId());
                    TopicSubjectTopic topicSubjectTopic = topicSubjectTopicMapper.selectOne(topicSubjectTopicLambdaQueryWrapper);
                    if (topicSubjectTopic != null) {
                        // 查询专题
                        TopicSubject topicSubject = topicSubjectMapper.selectById(topicSubjectTopic.getSubjectId());
                        if (topicSubject != null) {
                            // 判断数据库的专题和当前要修改的专题是否一致
                            if (!topicSubject.getSubjectName().equals(topicExcel.getSubjectName())) {
                                // 不一致更新当前专题被使用次数-1
                                topicSubject.setTopicCount(topicSubject.getTopicCount() - 1);
                                topicSubjectMapper.updateById(topicSubject);
                                // 然后查询当前要添加的专题
                                LambdaQueryWrapper<TopicSubject> topicSubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
                                topicSubjectLambdaQueryWrapper.eq(TopicSubject::getSubjectName, topicExcel.getSubjectName());
                                TopicSubject topicSubjectDb = topicSubjectMapper.selectOne(topicSubjectLambdaQueryWrapper);
                                if (topicSubjectDb != null) {
                                    topicSubjectDb.setTopicCount(topicSubjectDb.getTopicCount() + 1);
                                    topicSubjectMapper.updateById(topicSubjectDb);
                                    // 添加到题目关联专题表中
                                    TopicSubjectTopic topicSubjectTopicDb = new TopicSubjectTopic();
                                    topicSubjectTopicDb.setSubjectId(topicSubjectDb.getId());
                                    topicSubjectTopicDb.setTopicId(topic.getId());
                                    topicSubjectTopicMapper.insert(topicSubjectTopicDb);
                                }
                                // 删除以前的题目专题关联关系
                                topicSubjectTopicMapper.deleteById(topicSubjectTopic);
                            }
                        }
                    }

                    // 查询标签题目关系表
                    LambdaQueryWrapper<TopicLabelTopic> topicLabelTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    topicLabelTopicLambdaQueryWrapper.eq(TopicLabelTopic::getTopicId, topic.getId());
                    List<TopicLabelTopic> topicLabelTopics = topicLabelTopicMapper.selectList(topicLabelTopicLambdaQueryWrapper);
                    // 校验一下
                    if (CollectionUtils.isNotEmpty(topicLabelTopics)) {
                        // 获取所有的标签id
                        List<Long> labelIds = topicLabelTopics.stream()
                                .map(TopicLabelTopic::getLabelId)
                                .toList();
                        // 查询标签
                        List<TopicLabel> topicLabels = topicLabelMapper.selectBatchIds(labelIds);
                        // 更新所有标签次数-1
                        topicLabels.forEach(topicLabel -> {
                            topicLabel.setUseCount(topicLabel.getUseCount() - 1);
                            topicLabelMapper.updateById(topicLabel);
                        });
                        // 先删除题目关系表
                        topicLabelTopicMapper.delete(topicLabelTopicLambdaQueryWrapper);


                        // 校验要修改的标签名称是否与以前的名称是否一样
                        for (String labelName : labelNames) {
                            // 然后查询当前要添加的标签
                            LambdaQueryWrapper<TopicLabel> topicLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
                            topicLabelLambdaQueryWrapper.eq(TopicLabel::getLabelName, labelName);
                            TopicLabel topicLabelDb = topicLabelMapper.selectOne(topicLabelLambdaQueryWrapper);
                            if (topicLabelDb != null) {
                                topicLabelDb.setUseCount(topicLabelDb.getUseCount() + 1);
                                topicLabelMapper.updateById(topicLabelDb);
                                // 添加到题目关联标签表中
                                TopicLabelTopic topicLabelTopicDb = new TopicLabelTopic();
                                topicLabelTopicDb.setLabelId(topicLabelDb.getId());
                                topicLabelTopicDb.setTopicId(topic.getId());
                                topicLabelTopicMapper.insert(topicLabelTopicDb);
                            }
                        }
                    }

                    // 判断是否是开发者
                    if (currentId == 1L) {
                        // 是开发者不需要审核
                        topic.setStatus(StatusEnums.NORMAL.getCode());
                    } else {
                        // 不是开发者进行审核
                        topic.setStatus(StatusEnums.AUDITING.getCode());
                        // 异步发送消息给AI审核
                        TopicAudit topicAudit = new TopicAudit();
                        topicAudit.setTopicName(topicExcel.getTopicName());
                        topicAudit.setAnswer(topicExcel.getAnswer());
                        topicAudit.setAccount(username);
                        topicAudit.setUserId(currentId);
                        topicAudit.setTopicSubjectName(topicExcel.getSubjectName());
                        topicAudit.setTopicLabelName(topicExcel.getLabelName());
                        topicAudit.setId(topic.getId());
                        // 转换json
                        String topicAuditJson = JSON.toJSONString(topicAudit);
                        rabbitService.sendMessage(RabbitConstant.TOPIC_AUDIT_EXCHANGE, RabbitConstant.TOPIC_AUDIT_ROUTING_KEY_NAME, topicAuditJson);
                    }
                    topic.setFailMsg("");
                    // 更新
                    BeanUtils.copyProperties(topicExcel, topic);
                    topicMapper.updateById(topic);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("-题目：").append(topic.getTopicName()).append("-更新成功");
                } else {
                    // 已存在
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("-题目：").append(topic.getTopicName()).append("-已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "-题目： " + topicExcel.getTopicName() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new TopicException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 审核题目
     *
     * @param topic
     */
    public void auditTopic(Topic topic) {
        // 查询一下这个题目存不存在
        Topic topicDb = topicMapper.selectById(topic.getId());
        if (topicDb == null) {
            throw new TopicException(ResultCodeEnum.TOPIC_UPDATE_IS_NULL);
        }
        // 开始修改
        BeanUtils.copyProperties(topic, topicDb);
        // 如果是正常将失败原因置空
        if (Objects.equals(topic.getStatus(), StatusEnums.NORMAL.getCode())) {
            topic.setFailMsg("");
        }
        topic.setAiAnswer("");
        // 删除缓存
        stringRedisTemplate.delete(RedisConstant.GENERATE_ANSWER_KEY_PREFIX + topic.getId());
        topicMapper.updateById(topic);
    }

    /**
     * 生成ai答案
     *
     * @param id
     */
    public void generateAnswer(Long id) {
        // 防止重复生成
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.GENERATE_ANSWER_KEY_PREFIX + id);
        // 判断
        if (s == null || s.equals("")) {
            // 为空就存入
            stringRedisTemplate.opsForValue().set(RedisConstant.GENERATE_ANSWER_KEY_PREFIX + id, String.valueOf(id), 1, TimeUnit.DAYS);
        } else {
            throw new TopicException(ResultCodeEnum.TOPIC_GENERATE_ANSWER_PROCESSING);
        }
        // 查询一下这个题目存不存在
        Topic topicDb = topicMapper.selectById(id);
        if (topicDb == null) {
            throw new TopicException(ResultCodeEnum.TOPIC_GENERATE_ANSWER_ERROR);
        }
        // 发送消息给ai生成答案
        TopicAudit topicAudit = new TopicAudit();
        topicAudit.setTopicName(topicDb.getTopicName());
        topicAudit.setId(topicDb.getId());
        topicAudit.setUserId(SecurityUtils.getCurrentId());
        topicAudit.setAccount(SecurityUtils.getCurrentName());
        rabbitService.sendMessage(RabbitConstant.AI_ANSWER_EXCHANGE, RabbitConstant.AI_ANSWER_ROUTING_KEY_NAME, JSON.toJSONString(topicAudit));
    }

    /**
     * 修改
     *
     * @param topic
     */
    public void updateAiAnswer(Topic topic) {
        // 查询一下这个题目存不存在
        Topic topicDb = topicMapper.selectById(topic.getId());
        if (topicDb == null) {
            throw new TopicException(ResultCodeEnum.TOPIC_GENERATE_ANSWER_ERROR);
        }
        // 将答案封装
        topicDb.setAiAnswer(topic.getAiAnswer());
        topicMapper.updateById(topicDb);
    }

    /**
     * 根据题目id查询题目详细信息和标签
     *
     * @param id
     * @return
     */
    public TopicDetailVo detail(Long id) {
        if (id == null) {
            return null;
        }
        Topic topic = topicMapper.selectById(id);
        if (topic == null) {
            return null;
        }
        // 根据题目id查询题目标签题目关系表
        LambdaQueryWrapper<TopicLabelTopic> topicLabelTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicLabelTopicLambdaQueryWrapper.eq(TopicLabelTopic::getTopicId, id);
        List<TopicLabelTopic> topicLabelTopics = topicLabelTopicMapper.selectList(topicLabelTopicLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(topicLabelTopics)) {
            return null;
        }
        // 收集所有的id
        List<Long> labelIds = topicLabelTopics.stream().map(TopicLabelTopic::getLabelId).toList();
        // 存放标签名称
        List<String> labelNames = new ArrayList<>();
        // 查询
        for (Long labelId : labelIds) {
            LambdaQueryWrapper<TopicLabel> topicLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            topicLabelLambdaQueryWrapper.eq(TopicLabel::getId, labelId);
            topicLabelLambdaQueryWrapper.eq(TopicLabel::getStatus, StatusEnums.NORMAL.getCode());
            topicLabelLambdaQueryWrapper.orderByDesc(TopicLabel::getCreateTime);
            TopicLabel topicLabel = topicLabelMapper.selectOne(topicLabelLambdaQueryWrapper);
            if (topicLabel != null) {
                labelNames.add(topicLabel.getLabelName());
            }
        }
        TopicDetailVo topicDetailVo = new TopicDetailVo();
        BeanUtils.copyProperties(topic, topicDetailVo);
        topicDetailVo.setLabelNames(labelNames);
        // 查询这个题目id是否在redis
        boolean topicCollected = isTopicCollected(topic.getId());
        topicDetailVo.setIsCollected(topicCollected);
        return topicDetailVo;
    }

    public boolean isTopicCollected(Long topicId) {
        // 当前用户id
        Long userId = SecurityUtils.getCurrentId();
        // 收藏key
        String key = RedisConstant.USER_COLLECTIONS_PREFIX + userId;
        // 题目id
        String value = String.valueOf(topicId);

        // 查询 score，如果存在则返回非 null
        Double score = stringRedisTemplate.opsForZSet().score(key, value);

        return score != null;
    }

    /**
     * 获取答案
     *
     * @param id
     * @return
     */
    public TopicAnswerVo getAnswer(Long id) {
        if (id == null) {
            throw new TopicException(ResultCodeEnum.TOPIC_ANSWER_NOT_EXIST);
        }
        // 获取当前身份
        String role = SecurityUtils.getCurrentRole();
        // 是会员和管理员才能查看答案
        Topic topic = topicMapper.selectById(id);
        if (topic == null) {
            throw new TopicException(ResultCodeEnum.TOPIC_ANSWER_NOT_EXIST);
        }
        if (topic.getIsMember() == 1) {
            if (role.equals("member") || role.equals("admin")) {
                TopicAnswerVo topicAnswerVo = new TopicAnswerVo();
                BeanUtils.copyProperties(topic, topicAnswerVo);
                return topicAnswerVo;
            } else {
                throw new TopicException(ResultCodeEnum.TOPIC_MEMBER_ERROR);
            }
        } else {
            TopicAnswerVo topicAnswerVo = new TopicAnswerVo();
            BeanUtils.copyProperties(topic, topicAnswerVo);
            return topicAnswerVo;
        }
    }

    /**
     * 收藏和取消收藏题目
     *
     * @param id
     */
    public void collection(Long id) {
        if (id == null) {
            throw new TopicException(ResultCodeEnum.TOPIC_COLLECTION_ERROR);
        }
        Long userId = SecurityUtils.getCurrentId(); // 获取当前用户ID

        // 查询是否已收藏
        LambdaQueryWrapper<TopicCollection> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TopicCollection::getTopicId, id)
                .eq(TopicCollection::getUserId, userId);

        // 查询是否有不去查数据
        Long count = topicCollectionMapper.selectCount(wrapper);

        if (count > 0) {
            // 已收藏，执行取消收藏
            int delete = topicCollectionMapper.delete(wrapper);
            if (delete <= 0) {
                throw new TopicException(ResultCodeEnum.TOPIC_COLLECTION_ERROR);
            }
            try {
                // 同步更新Redis缓存
                stringRedisTemplate.opsForZSet().remove(RedisConstant.USER_COLLECTIONS_PREFIX + userId, String.valueOf(id));
            } catch (Exception e) {
                throw new TopicException(ResultCodeEnum.TOPIC_COLLECTION_ERROR);
            }
        } else {
            // 未收藏，执行收藏
            TopicCollection newCollection = new TopicCollection();
            newCollection.setTopicId(id);
            newCollection.setUserId(userId);
            int insert = topicCollectionMapper.insert(newCollection);
            if (insert <= 0) {
                throw new TopicException(ResultCodeEnum.TOPIC_COLLECTION_ERROR);
            }
            try {
                // 同步更新Redis缓存
                stringRedisTemplate.opsForZSet().add(RedisConstant.USER_COLLECTIONS_PREFIX + userId, String.valueOf(id), System.currentTimeMillis());
            } catch (Exception e) {
                throw new TopicException(ResultCodeEnum.TOPIC_COLLECTION_ERROR);
            }
        }
    }

    /**
     * 查询收藏的题目列表
     *
     * @return
     */
    public List<TopicCollectionVo> collectionList() {
        // 查询是否有收藏
        boolean userCollectionExists = isUserCollectionExists();
        if (!userCollectionExists) {
            return null;
        }

        // 有收藏，获取所有收藏的题目ID和分值
        Map<String, Double> topicIdScoreMap = getUserAllCollectionTopicIdsWithScores();
        if (topicIdScoreMap == null || topicIdScoreMap.isEmpty()) {
            return null;
        }

        // 提取题目ID列表用于数据库查询
        List<Long> topicIds = topicIdScoreMap.keySet().stream()
                .map(Long::valueOf)
                .toList();

        // 返回数据
        List<TopicCollectionVo> topicCollectionVos = new ArrayList<>();

        // 查询题目
        for (Long topicId : topicIds) {
            LambdaQueryWrapper<Topic> topicLambdaQueryWrapper = new LambdaQueryWrapper<>();
            topicLambdaQueryWrapper.eq(Topic::getId, topicId);
            topicLambdaQueryWrapper.eq(Topic::getStatus, StatusEnums.NORMAL.getCode());
            Topic topic = topicMapper.selectOne(topicLambdaQueryWrapper);
            if (topic != null) {
                TopicCollectionVo topicCollectionVo = new TopicCollectionVo();
                BeanUtils.copyProperties(topic, topicCollectionVo);
                // 根据题目id查询专题
                LambdaQueryWrapper<TopicSubjectTopic> topicSubjectTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicSubjectTopicLambdaQueryWrapper.eq(TopicSubjectTopic::getTopicId, topicId);
                TopicSubjectTopic topicSubjectTopic = topicSubjectTopicMapper.selectOne(topicSubjectTopicLambdaQueryWrapper);
                if (topicSubjectTopic != null) {
                    LambdaQueryWrapper<TopicSubject> topicSubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    topicSubjectLambdaQueryWrapper.eq(TopicSubject::getId, topicSubjectTopic.getSubjectId());
                    topicSubjectLambdaQueryWrapper.eq(TopicSubject::getStatus, StatusEnums.NORMAL.getCode());
                    // 查询专题
                    TopicSubject topicSubject = topicSubjectMapper.selectOne(topicSubjectLambdaQueryWrapper);
                    if (topicSubject != null) {
                        topicCollectionVo.setSubjectId(topicSubject.getId());
                    }
                }
                // 获取分值
                Double score = topicIdScoreMap.get(topicId.toString());
                if (score != null) {
                    // 转换为毫秒时间戳
                    Long timestamp = score.longValue();

                    // 转换为 LocalDateTime（默认系统时区）
                    LocalDateTime dateTime = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(timestamp),
                            ZoneId.systemDefault()
                    );

                    // 定义格式并格式化时间
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedTime = dateTime.format(formatter);

                    topicCollectionVo.setCollectionTime(formattedTime);
                }
                // 根据题目id查询题目标签题目关系表
                LambdaQueryWrapper<TopicLabelTopic> topicLabelTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicLabelTopicLambdaQueryWrapper.eq(TopicLabelTopic::getTopicId, topicId);
                List<TopicLabelTopic> topicLabelTopics = topicLabelTopicMapper.selectList(topicLabelTopicLambdaQueryWrapper);
                if (CollectionUtils.isEmpty(topicLabelTopics)) {
                    return null;
                }
                // 有标签
                // 收集所有的id
                List<Long> labelIds = topicLabelTopics.stream().map(TopicLabelTopic::getLabelId).toList();
                // 存放标签名称
                List<String> labelNames = new ArrayList<>();
                // 查询
                for (Long labelId : labelIds) {
                    LambdaQueryWrapper<TopicLabel> topicLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    topicLabelLambdaQueryWrapper.eq(TopicLabel::getId, labelId);
                    topicLabelLambdaQueryWrapper.eq(TopicLabel::getStatus, StatusEnums.NORMAL.getCode());
                    topicLabelLambdaQueryWrapper.orderByDesc(TopicLabel::getCreateTime);
                    TopicLabel topicLabel = topicLabelMapper.selectOne(topicLabelLambdaQueryWrapper);
                    if (topicLabel != null) {
                        labelNames.add(topicLabel.getLabelName());
                    }
                }
                topicCollectionVo.setLabelNames(labelNames);
                topicCollectionVos.add(topicCollectionVo);
            }
        }
        return topicCollectionVos;
    }


    /**
     * 是否有收藏key
     *
     * @return
     */
    public boolean isUserCollectionExists() {
        Long userId = SecurityUtils.getCurrentId();
        String key = RedisConstant.USER_COLLECTIONS_PREFIX + userId;

        Boolean hasKey = stringRedisTemplate.hasKey(key);
        return Boolean.TRUE.equals(hasKey);
    }

    /**
     * 获取用户收藏的题目ID与对应分值的映射
     *
     * @return
     */
    public Map<String, Double> getUserAllCollectionTopicIdsWithScores() {
        Long userId = SecurityUtils.getCurrentId();
        String key = RedisConstant.USER_COLLECTIONS_PREFIX + userId;

        // 获取 ZSet 中的所有元素及其分数
        Set<ZSetOperations.TypedTuple<String>> tuples = stringRedisTemplate.opsForZSet().rangeWithScores(key, 0, -1);

        if (tuples == null || tuples.isEmpty()) {
            return Collections.emptyMap();
        }

        // 转换为 Map<topicId, score>
        Map<String, Double> result = new HashMap<>();
        for (ZSetOperations.TypedTuple<String> tuple : tuples) {
            result.put(tuple.getValue(), tuple.getScore());
        }

        return result;
    }


    /**
     * 计算用户刷题次数
     */
    public void count(TopicRecordCountDto topicRecordCountDto) {
        // 校验参数
        if (topicRecordCountDto.getTopicId() == null || topicRecordCountDto.getSubjectId() == null) {
            return;
        }
        // 当前登录id
        Long userId = SecurityUtils.getCurrentId();
        // 获取用户身份
        String currentRole = SecurityUtils.getCurrentRole();
        String currentName;
        // 判断当前名称
        if (StringUtils.isEmpty(topicRecordCountDto.getNickname())) {
            // 获取当前登录名称
            currentName = SecurityUtils.getCurrentName();
        } else {
            currentName = topicRecordCountDto.getNickname();
        }
        // 当天日期
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // 查询记录表
        LambdaQueryWrapper<TopicRecord> topicRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicRecordLambdaQueryWrapper.eq(TopicRecord::getUserId, userId);
        topicRecordLambdaQueryWrapper.eq(TopicRecord::getTopicId, topicRecordCountDto.getTopicId());
        // 查询今日
        topicRecordLambdaQueryWrapper.eq(TopicRecord::getTopicTime, date);
        TopicRecord topicRecord = topicRecordMapper.selectOne(topicRecordLambdaQueryWrapper);
        if (topicRecord == null) {
            // 说明是第一次
            topicRecord = new TopicRecord();
            topicRecord.setTopicId(topicRecordCountDto.getTopicId());
            topicRecord.setSubjectId(topicRecordCountDto.getSubjectId());
            topicRecord.setCount(1L);
            topicRecord.setUserId(userId);
            topicRecord.setNickname(currentName);
            topicRecord.setTopicTime(new Date());
            topicRecordMapper.insert(topicRecord);
        } else {
            // 不是第一次刷这个题目
            topicRecord.setCount(topicRecord.getCount() + 1);
            topicRecord.setTopicTime(new Date());
            topicRecordMapper.updateById(topicRecord);
        }
        // 查询redis中是否有今日key
        Boolean hasKey = stringRedisTemplate.hasKey(RedisConstant.TOPIC_RANK_TODAY_PREFIX + date);
        if (Boolean.TRUE.equals(hasKey)) {
            // 存在今日，直接更新用户今日的做题总数
            stringRedisTemplate.opsForZSet().incrementScore(
                    RedisConstant.TOPIC_RANK_TODAY_PREFIX + date,
                    String.valueOf(userId),
                    1);
        } else {
            stringRedisTemplate.opsForZSet().add(RedisConstant.TOPIC_RANK_TODAY_PREFIX + date, String.valueOf(userId), 1);
        }
        // 查询redis中是否有改用户的总榜key
        Boolean aBoolean = stringRedisTemplate.hasKey(RedisConstant.TOPIC_RANK_PREFIX);
        if (Boolean.TRUE.equals(aBoolean)) {
            // 存在总榜，直接更新用户的做题总数
            stringRedisTemplate.opsForZSet().incrementScore(
                    RedisConstant.TOPIC_RANK_PREFIX,
                    String.valueOf(userId),
                    1);
        } else {
            // 不存在
            stringRedisTemplate.opsForZSet().add(RedisConstant.TOPIC_RANK_PREFIX, String.valueOf(userId), 1);
        }
        // 存储用户信息到Hash
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("nickname", currentName);
        userInfo.put("avatar", topicRecordCountDto.getAvatar());
        userInfo.put("role", currentRole);
        stringRedisTemplate.opsForHash().putAll(RedisConstant.USER_RANK_PREFIX + userId, userInfo);
    }


}
