package com.hao.topic.topic.service.impl;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.common.utils.StringUtils;
import com.hao.topic.model.dto.topic.TopicDto;
import com.hao.topic.model.dto.topic.TopicListDto;
import com.hao.topic.model.entity.topic.*;
import com.hao.topic.model.entity.topic.Topic;
import com.hao.topic.model.excel.topic.*;
import com.hao.topic.model.vo.topic.TopicVo;
import com.hao.topic.topic.enums.StatusEnums;
import com.hao.topic.topic.mapper.*;
import com.hao.topic.topic.service.TopicService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
        // 查询标签
        List<TopicLabel> topicLabels = topicLabelMapper.selectBatchIds(topicDto.getLabelIds());
        if (CollectionUtils.isEmpty(topicLabels)) {
            throw new TopicException(ResultCodeEnum.LABEL_NOT_EXIST);
        }

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
        // TODO 生成AI题目
        // TODO 异步发送信息给AI审核
        // 开始插入
        topicMapper.insert(topic);

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
        // 修改题目
        Topic topic = new Topic();
        BeanUtils.copyProperties(topicDto, topic);

        // 每日题目只能有9题
        if (topic.getIsEveryday() == 1) {
            LambdaQueryWrapper<Topic> topicLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            topicLambdaQueryWrapper1.eq(Topic::getIsEveryday, 1);
            List<Topic> topics = topicMapper.selectList(topicLambdaQueryWrapper1);
            if (CollectionUtils.isNotEmpty(topics) && topics.size() >= 9) {
                throw new TopicException(ResultCodeEnum.TOPIC_EVERYDAY_ERROR);
            }
        }
        // TODO 生成AI题目
        // TODO 异步发送信息给AI审核
        topic.setStatus(StatusEnums.AUDITING.getCode());
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
                    topicMapper.insert(topicDb);
                    // TODO 生成AI答案
                    // TODO 异步发送信息给AI审核
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
                    // 更新
                    BeanUtils.copyProperties(topicExcel, topic);
                    // 把状态修改为待审核
                    topic.setStatus(StatusEnums.AUDITING.getCode());
                    // 判断当前题目名称是否与导入的名称一样
                    if (!topic.getTopicName().equals(topicExcel.getTopicName())) {
                        // 不一样将当前AI答案滞空
                        topic.setAiAnswer("");
                        // TODO 生成AI答案
                        // TODO 异步发送信息给AI审核
                    }
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
                    topicMapper.insert(topicDb);
                    // TODO 生成AI答案
                    // TODO 异步发送信息给AI审核
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
                    // 更新
                    BeanUtils.copyProperties(topicExcel, topic);
                    // 把状态修改为待审核
                    topic.setStatus(StatusEnums.AUDITING.getCode());
                    // 判断当前题目名称是否与导入的名称一样
                    if (!topic.getTopicName().equals(topicExcel.getTopicName())) {
                        // 不一样将当前AI答案滞空
                        topic.setAiAnswer("");
                        // TODO 生成AI答案
                        // TODO 异步发送信息给AI审核
                    }
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

}
