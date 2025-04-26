package com.hao.topic.topic.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.topic.api.utils.constant.RabbitConstant;
import com.hao.topic.api.utils.enums.StatusEnums;
import com.hao.topic.api.utils.mq.RabbitService;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.common.utils.StringUtils;
import com.hao.topic.model.dto.audit.TopicAuditLabel;
import com.hao.topic.model.dto.topic.TopicLabelDto;
import com.hao.topic.model.dto.topic.TopicLabelListDto;
import com.hao.topic.model.entity.topic.*;
import com.hao.topic.model.entity.topic.TopicLabel;
import com.hao.topic.model.excel.topic.TopicLabelExcel;
import com.hao.topic.model.excel.topic.TopicLabelExcelExport;
import com.hao.topic.model.vo.topic.TopicLabelVo;
import com.hao.topic.topic.mapper.TopicLabelMapper;
import com.hao.topic.topic.mapper.TopicLabelTopicMapper;
import com.hao.topic.topic.service.TopicLabelService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private final RabbitService rabbitService;

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
        // 获取当前用户id
        Long currentId = SecurityUtils.getCurrentId();
        if (currentId == 1L) {
            // 是开发者不需要审核
            topicLabel.setStatus(StatusEnums.NORMAL.getCode());
            topicLabelMapper.insert(topicLabel);
        } else {
            // 不是开发者进行审核
            topicLabel.setStatus(StatusEnums.AUDITING.getCode());
            topicLabelMapper.insert(topicLabel);
            // 异步发送消息给AI审核
            TopicAuditLabel topicAuditLabel = new TopicAuditLabel();
            topicAuditLabel.setLabelName(topicLabelDto.getLabelName());
            topicAuditLabel.setAccount(username);
            topicAuditLabel.setUserId(currentId);
            topicAuditLabel.setId(topicLabel.getId());
            String topicAuditLabelJson = JSON.toJSONString(topicAuditLabel);
            log.info("发送消息{}", topicAuditLabelJson);
            rabbitService.sendMessage(RabbitConstant.LABEL_AUDIT_EXCHANGE, RabbitConstant.LABEL_AUDIT_ROUTING_KEY_NAME, topicAuditLabelJson);
        }
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
        // 判断是否要修改的名称是一样的
        if (!topicLabel.getLabelName().equals(topicLabelDto.getLabelName())) {
            // 不一样
            //  异步发送消息给AI审核
            // 判断是否是开发者
            Long currentId = SecurityUtils.getCurrentId();
            if (currentId == 1L) {
                // 是开发者不需要审核
                topicLabel.setStatus(StatusEnums.NORMAL.getCode());
            } else {
                // 不是开发者进行审核
                topicLabel.setStatus(StatusEnums.AUDITING.getCode());
                // 异步发送消息给AI审核
                TopicAuditLabel topicAuditLabel = new TopicAuditLabel();
                topicAuditLabel.setLabelName(topicLabelDto.getLabelName());
                topicAuditLabel.setAccount(SecurityUtils.getCurrentName());
                topicAuditLabel.setUserId(currentId);
                topicAuditLabel.setId(topicLabelDto.getId());
                String topicAuditLabelJson = JSON.toJSONString(topicAuditLabel);
                log.info("发送消息{}", topicAuditLabelJson);
                rabbitService.sendMessage(RabbitConstant.LABEL_AUDIT_EXCHANGE, RabbitConstant.LABEL_AUDIT_ROUTING_KEY_NAME, topicAuditLabelJson);
            }
            topicLabel.setFailMsg("");
        }
        // 开始修改
        topicLabel.setLabelName(topicLabelDto.getLabelName());
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

    /**
     * 导出excel
     *
     * @param topicLabelDto
     * @param ids
     * @return
     */
    public List<TopicLabelExcelExport> getExcelVo(TopicLabelListDto topicLabelDto, Long[] ids) {
        // 是否有id
        if (ids[0] != 0) {
            // 根据id查询
            List<TopicLabel> topicLabels = topicLabelMapper.selectBatchIds(Arrays.asList(ids));
            if (CollectionUtils.isEmpty(topicLabels)) {
                throw new TopicException(ResultCodeEnum.EXPORT_ERROR);
            }
            return topicLabels.stream().map(TopicLabel -> {
                TopicLabelExcelExport TopicLabelExcelExport = new TopicLabelExcelExport();
                BeanUtils.copyProperties(TopicLabel, TopicLabelExcelExport);
                // 状态特殊处理
                TopicLabelExcelExport.setStatus(StatusEnums.getMessageByCode(TopicLabel.getStatus()));
                return TopicLabelExcelExport;
            }).collect(Collectors.toList());
        } else {
            Map<String, Object> map = labelList(topicLabelDto);
            if (map.get("rows") == null) {
                throw new TopicException(ResultCodeEnum.EXPORT_ERROR);
            }
            List<TopicLabel> topicLabels = (List<TopicLabel>) map.get("rows");
            // 封装返回数据
            return topicLabels.stream().map(TopicLabel -> {
                TopicLabelExcelExport TopicLabelExcelExport = new TopicLabelExcelExport();
                BeanUtils.copyProperties(TopicLabel, TopicLabelExcelExport);
                // 状态特殊处理
                TopicLabelExcelExport.setStatus(StatusEnums.getMessageByCode(TopicLabel.getStatus()));
                return TopicLabelExcelExport;
            }).collect(Collectors.toList());
        }
    }

    /**
     * 导入excel
     *
     * @param multipartFile
     * @param updateSupport
     * @return
     */
    public String importExcel(MultipartFile multipartFile, Boolean updateSupport) throws IOException {
        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        Long currentId = SecurityUtils.getCurrentId();
        // 读取数据
        List<TopicLabelExcel> excelVoList = EasyExcel.read(multipartFile.getInputStream())
                // 映射数据
                .head(TopicLabelExcel.class)
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
        for (TopicLabelExcel topicLabelExcel : excelVoList) {
            try {
                LambdaQueryWrapper<TopicLabel> topicLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicLabelLambdaQueryWrapper.eq(TopicLabel::getLabelName, topicLabelExcel.getLabelName());
                TopicLabel topicLabel = topicLabelMapper.selectOne(topicLabelLambdaQueryWrapper);
                if (StringUtils.isNull(topicLabel)) {
                    // 不存在插入
                    TopicLabel topicLabelDb = new TopicLabel();
                    BeanUtils.copyProperties(topicLabelExcel, topicLabelDb);
                    topicLabelDb.setCreateBy(username);
                    // 判断是否为开发者
                    if (currentId == 1L) {
                        // 是开发者不需要审核
                        topicLabelDb.setStatus(StatusEnums.NORMAL.getCode());
                        topicLabelMapper.insert(topicLabelDb);
                    } else {
                        // 不是开发者需要审核
                        topicLabelDb.setStatus(StatusEnums.AUDITING.getCode());
                        topicLabelMapper.insert(topicLabelDb);
                        // 封装发送消息数据
                        TopicAuditLabel topicAuditLabel = new TopicAuditLabel();
                        // 封装审核消息
                        topicAuditLabel.setAccount(username);
                        topicAuditLabel.setUserId(currentId);
                        topicAuditLabel.setLabelName(topicLabelDb.getLabelName());
                        topicAuditLabel.setId(topicLabelDb.getId());
                        // 转换字符串
                        String jsonString = JSON.toJSONString(topicAuditLabel);
                        log.info("发送消息{}", jsonString);
                        // 异步调用发送消息给ai审核
                        rabbitService.sendMessage(RabbitConstant.LABEL_AUDIT_EXCHANGE, RabbitConstant.LABEL_AUDIT_ROUTING_KEY_NAME, jsonString);
                    }
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("-题目标签：").append(topicLabelDb.getLabelName()).append("-导入成功");
                } else if (updateSupport) {
                    // 判断要更新的名称和当前数据库的名称是否一致
                    if (!topicLabel.getLabelName().equals(topicLabelExcel.getLabelName())) {
                        // 不一致
                        if (currentId == 1L) {
                            // 是开发者不需要审核
                            topicLabel.setStatus(StatusEnums.NORMAL.getCode());
                        } else {
                            // 不是开发者需要审核
                            topicLabel.setStatus(StatusEnums.AUDITING.getCode());
                            // 封装发送消息数据
                            TopicAuditLabel topicAuditLabel = new TopicAuditLabel();
                            topicAuditLabel.setAccount(username);
                            topicAuditLabel.setUserId(currentId);
                            topicAuditLabel.setLabelName(topicLabelExcel.getLabelName());
                            topicAuditLabel.setId(topicLabel.getId());
                            String jsonString = JSON.toJSONString(topicAuditLabel);
                            log.info("发送消息{}", jsonString);
                            rabbitService.sendMessage(RabbitConstant.LABEL_AUDIT_EXCHANGE, RabbitConstant.LABEL_AUDIT_ROUTING_KEY_NAME, jsonString);
                        }
                        topicLabel.setFailMsg("");
                    }
                    // 更新
                    BeanUtils.copyProperties(topicLabelExcel, topicLabel);
                    topicLabelMapper.updateById(topicLabel);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("-题目标签：").append(topicLabel.getLabelName()).append("-更新成功");
                } else {
                    // 已存在
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("-题目标签：").append(topicLabel.getLabelName()).append("-已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "-题目标签： " + topicLabelExcel.getLabelName() + " 导入失败：";
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
     * 查询所有的标签名称和id
     *
     * @return
     */
    @Override
    public List<TopicLabelVo> list() {
        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        // 获取当前用户登录id
        Long currentId = SecurityUtils.getCurrentId();
        log.info("当前用户登录名称和id：{},{}", username, currentId);
        // 设置分页条件
        LambdaQueryWrapper<TopicLabel> topicLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicLabelLambdaQueryWrapper.eq(TopicLabel::getStatus, 0);
        // 判断是否为Hao
        if (currentId != 1L) {

        } else {
            topicLabelLambdaQueryWrapper.eq(TopicLabel::getCreateBy, username);
        }
        return topicLabelMapper.selectList(topicLabelLambdaQueryWrapper).stream().map(topicLabel -> {
            TopicLabelVo topicLabelVo = new TopicLabelVo();
            BeanUtils.copyProperties(topicLabel, topicLabelVo);
            return topicLabelVo;
        }).collect(Collectors.toList());
    }

    /**
     * 审核标签
     *
     * @param topicLabel
     */
    public void auditLabel(TopicLabel topicLabel) {
        // 查询一下这个分类存不存在
        TopicLabel topicLabelDb = topicLabelMapper.selectById(topicLabel.getId());
        if (topicLabelDb == null) {
            throw new TopicException(ResultCodeEnum.LABEL_NOT_EXIST);
        }
        // 开始修改
        BeanUtils.copyProperties(topicLabel, topicLabelDb);
        // 如果是正常将失败原因置空
        if (Objects.equals(topicLabel.getStatus(), StatusEnums.NORMAL.getCode())) {
            topicLabel.setFailMsg("");
        }
        topicLabelMapper.updateById(topicLabel);
    }
}
