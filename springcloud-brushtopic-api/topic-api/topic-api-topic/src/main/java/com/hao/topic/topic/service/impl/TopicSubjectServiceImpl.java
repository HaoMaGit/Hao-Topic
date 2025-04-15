package com.hao.topic.topic.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.common.utils.StringUtils;
import com.hao.topic.model.dto.topic.TopicSubjectDto;
import com.hao.topic.model.dto.topic.TopicSubjectListDto;
import com.hao.topic.model.entity.topic.TopicCategory;
import com.hao.topic.model.entity.topic.TopicCategorySubject;
import com.hao.topic.model.entity.topic.TopicSubject;
import com.hao.topic.model.entity.topic.TopicSubjectTopic;
import com.hao.topic.model.excel.topic.TopicCategoryExcel;
import com.hao.topic.model.excel.topic.TopicCategoryExcelExport;
import com.hao.topic.model.excel.topic.TopicSubjectExcel;
import com.hao.topic.model.excel.topic.TopicSubjectExcelExport;
import com.hao.topic.topic.enums.StatusEnums;
import com.hao.topic.topic.mapper.TopicCategoryMapper;
import com.hao.topic.topic.mapper.TopicCategorySubjectMapper;
import com.hao.topic.topic.mapper.TopicSubjectMapper;
import com.hao.topic.topic.mapper.TopicSubjectTopicMapper;
import com.hao.topic.topic.service.TopicSubjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
        topicSubjectMapper.insert(topicSubject);
        // TODO 异步发送消息给AI审核
        // 插入到关系表中
        TopicCategorySubject topicCategorySubject = new TopicCategorySubject();
        topicCategorySubject.setCategoryId(topicCategoryDb.getId());
        topicCategorySubject.setSubjectId(topicSubject.getId());
        topicCategorySubjectMapper.insert(topicCategorySubject);
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
        BeanUtils.copyProperties(topicSubjectDto, topicSubjectDb);
        //  TODO 异步发送消息给AI审核
        topicSubjectDb.setStatus(StatusEnums.AUDITING.getCode());
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
                    topicSubjectMapper.insert(topicSubjectDb);
                    TopicCategory topicCategory = topicCategoryMapper.selectOne(new LambdaQueryWrapper<TopicCategory>()
                            .eq(TopicCategory::getCategoryName, topicSubjectExcel.getCategoryName()));
                    // 插入到关联表
                    TopicCategorySubject topicCategorySubject = new TopicCategorySubject();
                    topicCategorySubject.setCategoryId(topicCategory.getId());
                    topicCategorySubject.setSubjectId(topicSubjectDb.getId());
                    if (topicCategorySubjectMapper != null) {
                        topicCategorySubjectMapper.insert(topicCategorySubject);
                    }
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("-题目专题：").append(topicSubjectDb.getSubjectName()).append("-导入成功");
                } else if (updateSupport) {
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
}
