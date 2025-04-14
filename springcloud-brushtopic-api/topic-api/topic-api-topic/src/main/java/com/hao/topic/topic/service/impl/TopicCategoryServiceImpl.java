package com.hao.topic.topic.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.common.utils.StringUtils;
import com.hao.topic.model.dto.topic.TopicCategoryDto;
import com.hao.topic.model.dto.topic.TopicCategoryListDto;
import com.hao.topic.model.entity.topic.TopicCategory;
import com.hao.topic.model.entity.topic.TopicCategorySubject;
import com.hao.topic.model.excel.topic.TopicCategoryExcel;
import com.hao.topic.model.excel.topic.TopicCategoryExcelExport;
import com.hao.topic.topic.enums.StatusEnums;
import com.hao.topic.topic.mapper.TopicCategoryMapper;
import com.hao.topic.topic.mapper.TopicCategorySubjectMapper;
import com.hao.topic.topic.service.TopicCategoryService;
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
 * Date: 2025/4/13 18:26
 */
@Service
@AllArgsConstructor
@Slf4j
public class TopicCategoryServiceImpl implements TopicCategoryService {
    private final TopicCategoryMapper topicCategoryMapper;
    private final TopicCategorySubjectMapper topicCategorySubjectMapper;

    /**
     * 分页查询分类列表
     *
     * @param topicCategoryDto
     * @return
     */
    public Map<String, Object> categoryList(TopicCategoryListDto topicCategoryDto) {
        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        // 获取当前用户登录id
        Long currentId = SecurityUtils.getCurrentId();
        log.info("当前用户登录名称和id：{},{}", username, currentId);
        // 设置分页条件
        LambdaQueryWrapper<TopicCategory> topicCategoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 判断是否为Hao
        if (currentId != 1L) {
            // 根据当前登录用户查询
            topicCategoryLambdaQueryWrapper.like(TopicCategory::getCreateBy, username);
        } else {
            // 是超管
            // 判断是否查询创建人
            if (!StringUtils.isEmpty(topicCategoryDto.getCreateBy())) {
                topicCategoryLambdaQueryWrapper.like(TopicCategory::getCreateBy, topicCategoryDto.getCreateBy());
            }
        }
        // 判断分类名称
        if (!StringUtils.isEmpty(topicCategoryDto.getCategoryName())) {
            topicCategoryLambdaQueryWrapper.like(TopicCategory::getCategoryName, topicCategoryDto.getCategoryName());
        }
        // 判断创建时间
        if (!StringUtils.isEmpty(topicCategoryDto.getBeginCreateTime()) && !StringUtils.isEmpty(topicCategoryDto.getEndCreateTime())) {
            topicCategoryLambdaQueryWrapper.between(TopicCategory::getCreateTime, topicCategoryDto.getBeginCreateTime(), topicCategoryDto.getEndCreateTime());
        }
        topicCategoryLambdaQueryWrapper.orderByDesc(TopicCategory::getCreateTime);
        if (topicCategoryDto.getPageNum() == null || topicCategoryDto.getPageSize() == null) {
            List<TopicCategory> categories = topicCategoryMapper.selectList(topicCategoryLambdaQueryWrapper);
            return Map.of("total", categories.size(), "rows", categories);
        } else {
            // 设置分页参数
            Page<TopicCategory> topicCategoryPage = new Page<>(topicCategoryDto.getPageNum(), topicCategoryDto.getPageSize());
            // 开始查询
            Page<TopicCategory> topicCategoryList = topicCategoryMapper.selectPage(topicCategoryPage, topicCategoryLambdaQueryWrapper);
            return Map.of("total", topicCategoryList.getTotal(), "rows", topicCategoryList.getRecords());
        }
    }

    /**
     * 添加题目分类
     *
     * @param topicCategoryDto
     */
    public void add(TopicCategoryDto topicCategoryDto) {
        // 校验分类名称
        if (StringUtils.isEmpty(topicCategoryDto.getCategoryName())) {
            throw new TopicException(ResultCodeEnum.CATEGORY_NAME_IS_NULL);
        }
        LambdaQueryWrapper<TopicCategory> topicCategoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        topicCategoryLambdaQueryWrapper.eq(TopicCategory::getCategoryName, topicCategoryDto.getCategoryName());
        // 查询
        TopicCategory topicCategoryDb = topicCategoryMapper.selectOne(topicCategoryLambdaQueryWrapper);
        if (topicCategoryDb != null) {
            throw new TopicException(ResultCodeEnum.CATEGORY_NAME_EXIST);
        }
        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        TopicCategory topicCategory = new TopicCategory();
        topicCategory.setCategoryName(topicCategoryDto.getCategoryName());
        topicCategory.setCreateBy(username);
        topicCategoryMapper.insert(topicCategory);
    }

    /**
     * 修改题目分类
     *
     * @param topicCategoryDto
     */
    public void update(TopicCategoryDto topicCategoryDto) {
        // 校验分类名称
        if (StringUtils.isEmpty(topicCategoryDto.getCategoryName())) {
            throw new TopicException(ResultCodeEnum.CATEGORY_NAME_IS_NULL);
        }
        // 查询
        TopicCategory topicCategory = topicCategoryMapper.selectById(topicCategoryDto.getId());
        if (topicCategory == null) {
            throw new TopicException(ResultCodeEnum.CATEGORY_UPDATE_IS_NULL);
        }
        // 开始修改
        topicCategory.setCategoryName(topicCategoryDto.getCategoryName());
        topicCategory.setStatus(StatusEnums.AUDITING.getCode());
        topicCategoryMapper.updateById(topicCategory);
    }

    /**
     * 删除题目分类
     *
     * @param ids
     */
    public void delete(Long[] ids) {
        // 校验
        if (ids == null) {
            throw new TopicException(ResultCodeEnum.CATEGORY_DELETE_IS_NULL);
        }
        for (Long id : ids) {
            LambdaQueryWrapper<TopicCategorySubject> topicCategorySubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
            // 查询分类与专题关系表
            topicCategorySubjectLambdaQueryWrapper.eq(TopicCategorySubject::getCategoryId, id);
            TopicCategorySubject topicCategorySubject = topicCategorySubjectMapper.selectOne(topicCategorySubjectLambdaQueryWrapper);
            if (topicCategorySubject != null) {
                throw new TopicException(ResultCodeEnum.CATEGORY_DELETE_TOPIC_ERROR);
            }
            // 删除
            topicCategoryMapper.deleteById(id);
        }
    }

    /**
     * 导出excel数据
     *
     * @param topicCategoryDto
     * @param ids
     * @return
     */
    public List<TopicCategoryExcelExport> getExcelVo(TopicCategoryListDto topicCategoryDto, Long[] ids) {
        // 是否有id
        if (ids[0] != 0) {
            // 根据id查询
            List<TopicCategory> topicCategories = topicCategoryMapper.selectBatchIds(Arrays.asList(ids));
            if (CollectionUtils.isEmpty(topicCategories)) {
                throw new TopicException(ResultCodeEnum.EXPORT_ERROR);
            }
            return topicCategories.stream().map(topicCategory -> {
                TopicCategoryExcelExport topicCategoryExcelExport = new TopicCategoryExcelExport();
                BeanUtils.copyProperties(topicCategory, topicCategoryExcelExport);
                // 状态特殊处理
                topicCategoryExcelExport.setStatus(StatusEnums.getMessageByCode(topicCategory.getStatus()));
                return topicCategoryExcelExport;
            }).collect(Collectors.toList());
        } else {
            Map<String, Object> map = categoryList(topicCategoryDto);
            if (map.get("rows") == null) {
                throw new TopicException(ResultCodeEnum.EXPORT_ERROR);
            }
            List<TopicCategory> categories = (List<TopicCategory>) map.get("rows");
            // 封装返回数据
            return categories.stream().map(topicCategory -> {
                TopicCategoryExcelExport topicCategoryExcelExport = new TopicCategoryExcelExport();
                BeanUtils.copyProperties(topicCategory, topicCategoryExcelExport);
                // 状态特殊处理
                topicCategoryExcelExport.setStatus(StatusEnums.getMessageByCode(topicCategory.getStatus()));
                return topicCategoryExcelExport;
            }).collect(Collectors.toList());
        }
    }

    /**
     * 导入excel数据
     *
     * @param multipartFile
     * @param updateSupport
     * @return
     */
    public String importExcel(MultipartFile multipartFile, Boolean updateSupport) throws IOException {
        // 获取当前用户登录名称
        String username = SecurityUtils.getCurrentName();
        // 读取数据
        List<TopicCategoryExcel> excelVoList = EasyExcel.read(multipartFile.getInputStream())
                // 映射数据
                .head(TopicCategoryExcel.class)
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
        for (TopicCategoryExcel topicCategoryExcel : excelVoList) {
            try {
                LambdaQueryWrapper<TopicCategory> topicCategoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
                topicCategoryLambdaQueryWrapper.eq(TopicCategory::getCategoryName, topicCategoryExcel.getCategoryName());
                TopicCategory topicCategory = topicCategoryMapper.selectOne(topicCategoryLambdaQueryWrapper);
                if (StringUtils.isNull(topicCategory)) {
                    // 不存在插入
                    TopicCategory topicCategoryDb = new TopicCategory();
                    BeanUtils.copyProperties(topicCategoryExcel, topicCategoryDb);
                    topicCategoryDb.setCreateBy(username);
                    topicCategoryMapper.insert(topicCategoryDb);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("-题目分类：").append(topicCategoryDb.getCategoryName()).append("-导入成功");
                } else if (updateSupport) {
                    // 更新
                    BeanUtils.copyProperties(topicCategoryExcel, topicCategory);
                    topicCategoryMapper.updateById(topicCategory);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("-题目分类：").append(topicCategory.getCategoryName()).append("-更新成功");
                } else {
                    // 已存在
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("-题目分类：").append(topicCategory.getCategoryName()).append("-已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "-题目分类： " + topicCategoryExcel.getCategoryName() + " 导入失败：";
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
