package com.hao.topic.topic.service.impl;

import com.alibaba.excel.EasyExcel;
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
import com.hao.topic.model.excel.topic.TopicLabelExcel;
import com.hao.topic.model.excel.topic.TopicLabelExcelExport;
import com.hao.topic.topic.enums.StatusEnums;
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
                    topicLabelMapper.insert(topicLabelDb);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("-题目标签：").append(topicLabelDb.getLabelName()).append("-导入成功");
                } else if (updateSupport) {
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
}
