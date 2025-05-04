package com.hao.topic.topic.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.topic.api.utils.constant.RabbitConstant;
import com.hao.topic.api.utils.enums.StatusEnums;
import com.hao.topic.api.utils.mq.RabbitService;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.common.utils.StringUtils;
import com.hao.topic.model.dto.audit.TopicAuditSubject;
import com.hao.topic.model.dto.topic.TopicSubjectDto;
import com.hao.topic.model.dto.topic.TopicSubjectListDto;
import com.hao.topic.model.entity.topic.*;
import com.hao.topic.model.excel.topic.TopicSubjectExcel;
import com.hao.topic.model.excel.topic.TopicSubjectExcelExport;
import com.hao.topic.model.vo.system.TopicSubjectWebVo;
import com.hao.topic.model.vo.topic.TopicCategoryVo;
import com.hao.topic.model.vo.topic.TopicNameVo;
import com.hao.topic.model.vo.topic.TopicSubjectDetailAndTopicVo;
import com.hao.topic.model.vo.topic.TopicSubjectVo;
import com.hao.topic.topic.mapper.*;
import com.hao.topic.topic.service.TopicSubjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/14 10:39
 */
@Service
@AllArgsConstructor
@Slf4j
public class TopicSubjectServiceImpl implements TopicSubjectService {
    private final TopicSubjectMapper topicSubjectMapper;
    private final TopicSubjectTopicMapper topicSubjectTopicMapper;
    private final TopicCategorySubjectMapper topicCategorySubjectMapper;
    private final TopicCategoryMapper topicCategoryMapper;
    private final RabbitService rabbitService;
    private final TopicMapper topicMapper;


    /**
     * 查询专题列表
     *
     * @param topicSubjectListDto
     * @return
     */
    public Map<String, Object> subjectList(TopicSubjectListDto topicSubjectListDto) {
        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        // 获取当前用户登录id
        Long currentId = SecurityUtils.getCurrentId();
        log.info("当前用户登录名称和id：{},{}", username, currentId);
        // 设置分页条件
        LambdaQueryWrapper<TopicSubject> topicSubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 判断是否为Hao
        if (currentId != 1L) {
            // 根据当前登录用户查询
            topicSubjectLambdaQueryWrapper.like(TopicSubject::getCreateBy, username);
        } else {
            // 是超管
            // 判断是否查询创建人
            if (!StringUtils.isEmpty(topicSubjectListDto.getCreateBy())) {
                topicSubjectLambdaQueryWrapper.like(TopicSubject::getCreateBy, topicSubjectListDto.getCreateBy());
            }
        }
        // 判断专题名称
        if (!StringUtils.isEmpty(topicSubjectListDto.getSubjectName())) {
            topicSubjectLambdaQueryWrapper.like(TopicSubject::getSubjectName, topicSubjectListDto.getSubjectName());
        }
        // 判断创建时间
        if (!StringUtils.isEmpty(topicSubjectListDto.getBeginCreateTime()) && !StringUtils.isEmpty(topicSubjectListDto.getEndCreateTime())) {
            topicSubjectLambdaQueryWrapper.between(TopicSubject::getCreateTime, topicSubjectListDto.getBeginCreateTime(), topicSubjectListDto.getEndCreateTime());
        }
        topicSubjectLambdaQueryWrapper.orderByDesc(TopicSubject::getCreateTime);
        // 开始分页查询
        if (topicSubjectListDto.getPageNum() == null || topicSubjectListDto.getPageSize() == null) {
            List<TopicSubject> topicSubjects = topicSubjectMapper.selectList(topicSubjectLambdaQueryWrapper);
            // 封装返回结果
            for (TopicSubject topicSubject : topicSubjects) {
                // 根据专题id查询分类专题表
                LambdaQueryWrapper<TopicCategorySubject> topicCategorySubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicCategorySubjectLambdaQueryWrapper.eq(TopicCategorySubject::getSubjectId, topicSubject.getId());
                TopicCategorySubject topicCategorySubject = topicCategorySubjectMapper.selectOne(topicCategorySubjectLambdaQueryWrapper);
                if (topicCategorySubject != null) {
                    TopicCategory topicCategory = topicCategoryMapper.selectById(topicCategorySubject.getCategoryId());
                    if (topicCategory != null) {
                        topicSubject.setCategoryName(topicCategory.getCategoryName());
                    }
                }
            }
            // 校验参数
            if (topicSubjectListDto.getCategoryName() != null) {
                // 模糊匹配过滤分类名称
                topicSubjects.removeIf(topicSubject -> !topicSubject.getCategoryName().contains(topicSubjectListDto.getCategoryName()));
            }
            return Map.of("total", topicSubjects.size(), "rows", topicSubjects);
        } else {
            // 设置分页参数
            Page<TopicSubject> topicSubjectPage = new Page<>(topicSubjectListDto.getPageNum(), topicSubjectListDto.getPageSize());
            // 开始查询
            Page<TopicSubject> topicSubjectPageResult = topicSubjectMapper.selectPage(topicSubjectPage, topicSubjectLambdaQueryWrapper);
            topicSubjectPageResult.getRecords().forEach(topicSubject -> {
                // 根据专题id查询分类专题表
                LambdaQueryWrapper<TopicCategorySubject> topicCategorySubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicCategorySubjectLambdaQueryWrapper.eq(TopicCategorySubject::getSubjectId, topicSubject.getId());
                TopicCategorySubject topicCategorySubject = topicCategorySubjectMapper.selectOne(topicCategorySubjectLambdaQueryWrapper);
                if (topicCategorySubject != null) {
                    TopicCategory topicCategory = topicCategoryMapper.selectById(topicCategorySubject.getCategoryId());
                    if (topicCategory != null) {
                        topicSubject.setCategoryName(topicCategory.getCategoryName());
                    }
                }
            });
            // 校验参数
            if (topicSubjectListDto.getCategoryName() != null) {
                // 模糊匹配过滤分类名称
                topicSubjectPageResult.getRecords().removeIf(topicSubject -> !topicSubject.getCategoryName().contains(topicSubjectListDto.getCategoryName()));
            }
            return Map.of("total", topicSubjectPageResult.getTotal(), "rows", topicSubjectPageResult.getRecords());
        }
    }

    /**
     * 新增专题
     *
     * @param topicSubjectDto
     */
    @Transactional
    public void add(TopicSubjectDto topicSubjectDto) {
        // 查询
        TopicSubject topicSubjectDb = topicSubjectMapper.selectById(topicSubjectDto.getId());
        if (topicSubjectDb != null) {
            throw new TopicException(ResultCodeEnum.SUBJECT_NAME_EXIST);
        }
        //  根据分类名称查询分类
        TopicCategory topicCategoryDb = topicCategoryMapper
                .selectOne(new LambdaQueryWrapper<TopicCategory>().
                        eq(TopicCategory::getCategoryName, topicSubjectDto.getCategoryName()));
        if (topicCategoryDb == null) {
            throw new TopicException(ResultCodeEnum.SUBJECT_SELECT_ERROR);
        }
        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        TopicSubject topicSubject = new TopicSubject();
        BeanUtils.copyProperties(topicSubjectDto, topicSubject);
        topicSubject.setCreateBy(username);

        // 获取当前id
        Long currentId = SecurityUtils.getCurrentId();
        if (currentId == 1L) {
            // 是开发者不需要审核
            topicSubject.setStatus(StatusEnums.NORMAL.getCode());
            topicSubjectMapper.insert(topicSubject);

        } else {
            // 不是开发者需要审核
            topicSubject.setStatus(StatusEnums.AUDITING.getCode());
            topicSubjectMapper.insert(topicSubject);
            // 封装消息对象
            TopicAuditSubject topicAuditSubject = new TopicAuditSubject();
            topicAuditSubject.setSubjectName(topicSubject.getSubjectName());
            topicAuditSubject.setCategoryName(topicCategoryDb.getCategoryName());
            topicAuditSubject.setId(topicSubject.getId());
            topicAuditSubject.setSubjectDesc(topicSubject.getSubjectDesc());
            topicAuditSubject.setAccount(username);
            topicAuditSubject.setUserId(currentId);
            // 转换json
            String topicAuditSubjectJson = JSON.toJSONString(topicAuditSubject);
            log.info("发送消息{}", topicAuditSubjectJson);
            // 异步发送消息给AI审核
            rabbitService.sendMessage(RabbitConstant.SUBJECT_AUDIT_EXCHANGE, RabbitConstant.SUBJECT_AUDIT_ROUTING_KEY_NAME, topicAuditSubjectJson);
        }

        // 插入到关系表中
        TopicCategorySubject topicCategorySubject = new TopicCategorySubject();
        topicCategorySubject.setCategoryId(topicCategoryDb.getId());
        topicCategorySubject.setSubjectId(topicSubject.getId());
        topicCategorySubjectMapper.insert(topicCategorySubject);

        topicCategoryDb.setSubjectCount(topicCategoryDb.getSubjectCount() + 1);
        // 更新分类专题数量
        topicCategoryMapper.updateById(topicCategoryDb);
    }


    /**
     * 修改题目专题
     *
     * @param topicSubjectDto
     */
    @Transactional
    public void update(TopicSubjectDto topicSubjectDto) {
        // 查询
        TopicSubject topicSubjectDb = topicSubjectMapper.selectById(topicSubjectDto.getId());
        if (topicSubjectDb == null) {
            throw new TopicException(ResultCodeEnum.SUBJECT_UPDATE_IS_NULL);
        }
        //  根据分类名称查询分类
        TopicCategory topicCategoryDb = topicCategoryMapper
                .selectOne(new LambdaQueryWrapper<TopicCategory>().
                        eq(TopicCategory::getCategoryName, topicSubjectDto.getCategoryName()));
        if (topicCategoryDb == null) {
            throw new TopicException(ResultCodeEnum.SUBJECT_SELECT_ERROR);
        }
        // 删除关系表
        topicCategorySubjectMapper
                .delete(new LambdaQueryWrapper<TopicCategorySubject>()
                        .eq(TopicCategorySubject::getSubjectId, topicSubjectDto.getId()));
        // 判断当前要修改的名称是否和数据库的名称一样
        if (!topicSubjectDb.getSubjectName().equals(topicSubjectDto.getSubjectName())
                || !topicSubjectDb.getSubjectDesc().equals(topicSubjectDto.getSubjectDesc())) {
            // 不一致就要判断是否是开发者
            Long currentId = SecurityUtils.getCurrentId();
            if (currentId == 1L) {
                // 是开发者不需要审核
                topicSubjectDb.setStatus(StatusEnums.NORMAL.getCode());
            } else {
                // 不是开发者需要审核
                topicSubjectDb.setStatus(StatusEnums.AUDITING.getCode());
                // 异步发送消息给AI审核
                TopicAuditSubject topicAuditSubject = new TopicAuditSubject();
                topicAuditSubject.setSubjectName(topicSubjectDb.getSubjectName());
                topicAuditSubject.setCategoryName(topicCategoryDb.getCategoryName());
                topicAuditSubject.setUserId(currentId);
                topicAuditSubject.setAccount(SecurityUtils.getCurrentName());
                topicAuditSubject.setId(topicSubjectDb.getId());
                topicAuditSubject.setSubjectDesc(topicSubjectDto.getSubjectDesc());
                String topicAuditSubjectJson = JSON.toJSONString(topicAuditSubject);
                log.info("发送消息{}", topicAuditSubjectJson);
                rabbitService.sendMessage(RabbitConstant.SUBJECT_AUDIT_EXCHANGE, RabbitConstant.SUBJECT_AUDIT_ROUTING_KEY_NAME, topicAuditSubjectJson);
            }
            topicSubjectDb.setFailMsg("");
        }
        BeanUtils.copyProperties(topicSubjectDto, topicSubjectDb);
        topicSubjectMapper.updateById(topicSubjectDb);
        // 插入到关系表中
        TopicCategorySubject topicCategorySubject = new TopicCategorySubject();
        topicCategorySubject.setCategoryId(topicCategoryDb.getId());
        topicCategorySubject.setSubjectId(topicSubjectDb.getId());
        topicCategorySubjectMapper.insert(topicCategorySubject);
    }


    /**
     * 删除题目专题
     *
     * @param ids
     */
    @Override
    public void delete(Long[] ids) {
        // 校验
        if (ids == null) {
            throw new TopicException(ResultCodeEnum.SUBJECT_DELETE_IS_NULL);
        }
        for (Long id : ids) {
            // 查询题目与专题关系表
            LambdaQueryWrapper<TopicSubjectTopic> topicSubjectTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
            topicSubjectTopicLambdaQueryWrapper.eq(TopicSubjectTopic::getSubjectId, id);
            TopicSubjectTopic topicSubjectTopic = topicSubjectTopicMapper.selectOne(topicSubjectTopicLambdaQueryWrapper);
            if (topicSubjectTopic != null) {
                throw new TopicException(ResultCodeEnum.SUBJECT_DELETE_TOPIC_ERROR);
            }
            // 查询分类与专题关系表
            LambdaQueryWrapper<TopicCategorySubject> topicCategorySubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
            topicCategorySubjectLambdaQueryWrapper.eq(TopicCategorySubject::getSubjectId, id);
            TopicCategorySubject topicCategorySubject = topicCategorySubjectMapper.selectOne(topicCategorySubjectLambdaQueryWrapper);
            if (topicCategorySubject != null) {
                TopicCategory topicCategory = topicCategoryMapper.selectById(topicCategorySubject.getCategoryId());
                if (topicCategory != null) {
                    topicCategory.setSubjectCount(topicCategory.getSubjectCount() - 1);
                    // 更新分类专题数量
                    topicCategoryMapper.updateById(topicCategory);
                }
            }
            // 删除分类与专题关系表
            topicCategorySubjectMapper.delete(topicCategorySubjectLambdaQueryWrapper);
            // 删除
            topicSubjectMapper.deleteById(id);
        }
    }

    /**
     * 导出excel
     *
     * @param topicSubjectListDto
     * @param ids
     * @return
     */
    public List<TopicSubjectExcelExport> getExcelVo(TopicSubjectListDto topicSubjectListDto, Long[] ids) {
        // 是否有id
        if (ids[0] != 0) {
            // 根据id查询
            List<TopicSubject> topicSubjects = topicSubjectMapper.selectBatchIds(Arrays.asList(ids));
            if (CollectionUtils.isEmpty(topicSubjects)) {
                throw new TopicException(ResultCodeEnum.EXPORT_ERROR);
            }
            return topicSubjects.stream().map(topicSubject -> {
                TopicSubjectExcelExport topicSubjectExcelExport = new TopicSubjectExcelExport();
                BeanUtils.copyProperties(topicSubject, topicSubjectExcelExport);
                // 状态特殊处理
                topicSubjectExcelExport.setStatus(StatusEnums.getMessageByCode(topicSubject.getStatus()));
                // 分类名称特殊处理
                // 根据专题id查询分类专题表
                LambdaQueryWrapper<TopicCategorySubject> topicCategorySubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicCategorySubjectLambdaQueryWrapper.eq(TopicCategorySubject::getSubjectId, topicSubject.getId());
                TopicCategorySubject topicCategorySubject = topicCategorySubjectMapper.selectOne(topicCategorySubjectLambdaQueryWrapper);
                if (topicCategorySubject != null) {
                    TopicCategory topicCategory = topicCategoryMapper.selectById(topicCategorySubject.getCategoryId());
                    if (topicCategory != null) {
                        topicSubjectExcelExport.setCategoryName(topicCategory.getCategoryName());
                    }
                }
                return topicSubjectExcelExport;
            }).collect(Collectors.toList());
        } else {
            Map<String, Object> map = subjectList(topicSubjectListDto);
            if (map.get("rows") == null) {
                throw new TopicException(ResultCodeEnum.EXPORT_ERROR);
            }
            List<TopicSubject> categories = (List<TopicSubject>) map.get("rows");
            // 封装返回数据
            return categories.stream().map(topicSubject -> {
                TopicSubjectExcelExport topicSubjectExcelExport = new TopicSubjectExcelExport();
                BeanUtils.copyProperties(topicSubject, topicSubjectExcelExport);
                // 状态特殊处理
                topicSubjectExcelExport.setStatus(StatusEnums.getMessageByCode(topicSubject.getStatus()));
                return topicSubjectExcelExport;
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
    @Transactional
    public String importExcel(MultipartFile multipartFile, Boolean updateSupport) throws IOException {
        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        Long currentId = SecurityUtils.getCurrentId();
        // 读取数据
        List<TopicSubjectExcel> excelVoList = EasyExcel.read(multipartFile.getInputStream())
                // 映射数据
                .head(TopicSubjectExcel.class)
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
        // 校验参数
        for (TopicSubjectExcel topicSubjectExcel : excelVoList) {
            if (StringUtils.isNull(topicSubjectExcel.getSubjectName()) || StringUtils.isNull(topicSubjectExcel.getSubjectDesc()) || StringUtils.isNull(topicSubjectExcel.getImageUrl()) || StringUtils.isNull(topicSubjectExcel.getCategoryName())) {
                throw new TopicException(ResultCodeEnum.IMPORT_ERROR);
            }
            // 查询分类是否存在
            TopicCategory topicCategoryDb = topicCategoryMapper
                    .selectOne(new LambdaQueryWrapper<TopicCategory>().
                            eq(TopicCategory::getCategoryName, topicSubjectExcel.getCategoryName()));
            if (topicCategoryDb == null) {
                failureNum++;
                String msg = "<br/>" + failureNum + "-题目专题： " + topicSubjectExcel.getSubjectName() + " 导入失败题目分类不存在：";
                failureMsg.append(msg);
                throw new TopicException(failureMsg.toString());
            }
        }

        // 遍历
        for (TopicSubjectExcel topicSubjectExcel : excelVoList) {
            try {
                LambdaQueryWrapper<TopicSubject> topicSubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicSubjectLambdaQueryWrapper.eq(TopicSubject::getSubjectName, topicSubjectExcel.getSubjectName());
                TopicSubject topicSubject = topicSubjectMapper.selectOne(topicSubjectLambdaQueryWrapper);
                if (StringUtils.isNull(topicSubject)) {
                    // 不存在插入
                    TopicSubject topicSubjectDb = new TopicSubject();
                    BeanUtils.copyProperties(topicSubjectExcel, topicSubjectDb);
                    topicSubjectDb.setCreateBy(username);
                    // 判断是否为开发者
                    if (currentId == 1L) {
                        // 是开发者不需要审核
                        topicSubjectDb.setStatus(StatusEnums.NORMAL.getCode());
                        topicSubjectMapper.insert(topicSubjectDb);
                    } else {
                        // 不是开发者需要审核
                        topicSubjectDb.setStatus(StatusEnums.AUDITING.getCode());
                        topicSubjectMapper.insert(topicSubjectDb);
                        // 封装发送消息数据
                        TopicAuditSubject topicAuditSubject = new TopicAuditSubject();
                        topicAuditSubject.setSubjectName(topicSubjectDb.getSubjectName());
                        topicAuditSubject.setCategoryName(topicSubjectExcel.getCategoryName());
                        topicAuditSubject.setId(topicSubjectDb.getId());
                        topicAuditSubject.setAccount(SecurityUtils.getCurrentName());
                        topicAuditSubject.setSubjectDesc(topicSubjectDb.getSubjectDesc());
                        topicAuditSubject.setUserId(currentId);
                        String topicAuditSubjectJson = JSON.toJSONString(topicAuditSubject);
                        log.info("发送消息{}", topicAuditSubjectJson);
                        rabbitService.sendMessage(RabbitConstant.SUBJECT_AUDIT_EXCHANGE, RabbitConstant.SUBJECT_AUDIT_ROUTING_KEY_NAME, topicAuditSubjectJson);
                    }
                    TopicCategory topicCategory = topicCategoryMapper.selectOne(new LambdaQueryWrapper<TopicCategory>()
                            .eq(TopicCategory::getCategoryName, topicSubjectExcel.getCategoryName()));
                    // 插入到关联表
                    TopicCategorySubject topicCategorySubject = new TopicCategorySubject();
                    topicCategorySubject.setCategoryId(topicCategory.getId());
                    topicCategorySubject.setSubjectId(topicSubjectDb.getId());
                    if (topicCategorySubjectMapper != null) {
                        topicCategorySubjectMapper.insert(topicCategorySubject);
                    }
                    topicCategory.setSubjectCount(topicCategory.getSubjectCount() + 1);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("-题目专题：").append(topicSubjectDb.getSubjectName()).append("-导入成功");
                } else if (updateSupport) {
                    // 判断是否为开发者
                    if (currentId == 1L) {
                        // 是开发者不需要审核
                        topicSubject.setStatus(StatusEnums.NORMAL.getCode());
                    } else {
                        // 不是开发者判断当前要修改的名称和数据库的名称是否一样
                        if (!topicSubjectExcel.getSubjectName().equals(topicSubject.getSubjectName())
                                || !topicSubjectExcel.getSubjectDesc().equals(topicSubject.getSubjectDesc())
                        ) {
                            // 不是同一个名开始审核
                            // 封装发送消息数据
                            TopicAuditSubject topicAuditSubject = new TopicAuditSubject();
                            topicAuditSubject.setSubjectName(topicSubjectExcel.getSubjectName());
                            topicAuditSubject.setCategoryName(topicSubjectExcel.getCategoryName());
                            topicAuditSubject.setId(topicSubject.getId());
                            topicAuditSubject.setAccount(SecurityUtils.getCurrentName());
                            topicAuditSubject.setUserId(currentId);
                            topicAuditSubject.setSubjectDesc(topicSubjectExcel.getSubjectDesc());
                            String topicAuditSubjectJson = JSON.toJSONString(topicAuditSubject);
                            log.info("发送消息{}", topicAuditSubjectJson);
                            rabbitService.sendMessage(RabbitConstant.SUBJECT_AUDIT_EXCHANGE, RabbitConstant.SUBJECT_AUDIT_ROUTING_KEY_NAME, topicAuditSubjectJson);
                        }
                        topicSubject.setFailMsg("");
                    }
                    // 更新
                    BeanUtils.copyProperties(topicSubjectExcel, topicSubject);
                    topicSubjectMapper.updateById(topicSubject);
                    // 删除关联表
                    LambdaQueryWrapper<TopicCategorySubject> topicCategorySubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    topicCategorySubjectLambdaQueryWrapper.eq(TopicCategorySubject::getSubjectId, topicSubject.getId());
                    topicCategorySubjectMapper.delete(topicCategorySubjectLambdaQueryWrapper);
                    TopicCategory topicCategory = topicCategoryMapper.selectOne(new LambdaQueryWrapper<TopicCategory>()
                            .eq(TopicCategory::getCategoryName, topicSubjectExcel.getCategoryName()));
                    // 插入
                    TopicCategorySubject topicCategorySubject = new TopicCategorySubject();
                    topicCategorySubject.setCategoryId(topicCategory.getId());
                    topicCategorySubject.setSubjectId(topicSubject.getId());
                    topicCategorySubjectMapper.insert(topicCategorySubject);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("-题目专题：").append(topicSubject.getSubjectName()).append("-更新成功");
                } else {
                    // 已存在
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("-题目专题：").append(topicSubject.getSubjectName()).append("-已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "-题目专题： " + topicSubjectExcel.getSubjectName() + " 导入失败：";
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
     * 查询所有的专题名称和id
     *
     * @return
     */
    public List<TopicSubjectVo> list() {
        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        // 获取当前用户登录id
        Long currentId = SecurityUtils.getCurrentId();
        log.info("当前用户登录名称和id：{},{}", username, currentId);
        // 设置分页条件
        LambdaQueryWrapper<TopicSubject> topicSubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicSubjectLambdaQueryWrapper.eq(TopicSubject::getStatus, 0);
        // 判断是否为Hao
        if (currentId != 1L) {

        } else {
            topicSubjectLambdaQueryWrapper.eq(TopicSubject::getCreateBy, username);
        }
        return topicSubjectMapper.selectList(topicSubjectLambdaQueryWrapper).stream().map(topicSubject -> {
            TopicSubjectVo topicSubjectVo = new TopicSubjectVo();
            BeanUtils.copyProperties(topicSubject, topicSubjectVo);
            return topicSubjectVo;
        }).collect(Collectors.toList());
    }

    /**
     * 审核题目专题
     *
     * @param topicSubject
     */
    public void auditSubject(TopicSubject topicSubject) {
        // 查询一下这个分类存不存在
        TopicSubject topicSubjectDb = topicSubjectMapper.selectById(topicSubject.getId());
        if (topicSubjectDb == null) {
            throw new TopicException(ResultCodeEnum.SUBJECT_UPDATE_IS_NULL);
        }
        // 开始修改
        BeanUtils.copyProperties(topicSubject, topicSubjectDb);
        // 如果是正常将失败原因置空
        if (Objects.equals(topicSubject.getStatus(), StatusEnums.NORMAL.getCode())) {
            topicSubject.setFailMsg("");
        }
        topicSubjectMapper.updateById(topicSubject);
    }


    /**
     * 根据分类id查询
     *
     * @param categoryId
     * @return
     */
    public List<TopicSubjectWebVo> subject(Long categoryId) {
        List<TopicSubjectWebVo> topicSubjectWebVos = new ArrayList<>();
        // 查询分类专题表
        LambdaQueryWrapper<TopicCategorySubject> topicCategorySubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicCategorySubjectLambdaQueryWrapper.eq(TopicCategorySubject::getCategoryId, categoryId);
        List<TopicCategorySubject> topicCategorySubjects = topicCategorySubjectMapper.selectList(topicCategorySubjectLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(topicCategorySubjects)) {
            return null;
        }
        // 获取所有的专题id
        List<Long> subjectIds = topicCategorySubjects.stream().map(TopicCategorySubject::getSubjectId).toList();
        // 查询专题表
        for (Long subjectId : subjectIds) {
            LambdaQueryWrapper<TopicSubject> topicSubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
            topicSubjectLambdaQueryWrapper.eq(TopicSubject::getId, subjectId);
            topicSubjectLambdaQueryWrapper.eq(TopicSubject::getStatus, StatusEnums.NORMAL.getCode());
            TopicSubject topicSubject = topicSubjectMapper.selectOne(topicSubjectLambdaQueryWrapper);
            if (topicSubject != null) {
                TopicSubjectWebVo topicSubjectWebVo = new TopicSubjectWebVo();
                BeanUtils.copyProperties(topicSubject, topicSubjectWebVo);
                topicSubjectWebVos.add(topicSubjectWebVo);
            }
        }
        // 根据id排序
        topicSubjectWebVos.sort(Comparator.comparing(TopicSubjectWebVo::getId));
        return topicSubjectWebVos;
    }

    /**
     * 根据专题id查询专题详情和题目列表
     *
     * @param id
     * @return
     */
    public TopicSubjectDetailAndTopicVo subjectDetail(Long id) {
        // 查询专题
        if (id == null) {
            return null;
        }
        TopicSubject topicSubject = topicSubjectMapper.selectById(id);
        if (topicSubject == null) {
            return null;
        }
        TopicSubjectDetailAndTopicVo topicSubjectDetailAndTopicVo = new TopicSubjectDetailAndTopicVo();
        BeanUtils.copyProperties(topicSubject, topicSubjectDetailAndTopicVo);
        // 查询题目专题关系表
        LambdaQueryWrapper<TopicSubjectTopic> topicSubjectTopicLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicSubjectTopicLambdaQueryWrapper.eq(TopicSubjectTopic::getSubjectId, id);
        List<TopicSubjectTopic> topicSubjectTopics = topicSubjectTopicMapper.selectList(topicSubjectTopicLambdaQueryWrapper);
        // 题目列表
        List<TopicNameVo> topicNameVos = new ArrayList<>();
        // 查询题目
        for (TopicSubjectTopic topicSubjectTopic : topicSubjectTopics) {
            LambdaQueryWrapper<Topic> topicLambdaQueryWrapper = new LambdaQueryWrapper<>();
            topicLambdaQueryWrapper.eq(Topic::getId, topicSubjectTopic.getTopicId());
            topicLambdaQueryWrapper.eq(Topic::getStatus, StatusEnums.NORMAL.getCode());
            topicLambdaQueryWrapper.orderByDesc(Topic::getSorted);
            topicLambdaQueryWrapper.orderByDesc(Topic::getCreateTime);
            Topic topic = topicMapper.selectOne(topicLambdaQueryWrapper);
            if (topic != null) {
                TopicNameVo topicNameVo = new TopicNameVo();
                topicNameVo.setTopicName(topic.getTopicName());
                topicNameVo.setId(topic.getId());
                topicNameVos.add(topicNameVo);
            }
        }
        topicSubjectDetailAndTopicVo.setTopicNameVos(topicNameVos);
        return topicSubjectDetailAndTopicVo;
    }
}
