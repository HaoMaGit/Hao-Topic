package com.hao.topic.ai.service.impl;

import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesisParam;
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesizer;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.util.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.topic.ai.constant.AiConstant;
import com.hao.topic.ai.constant.PromptConstant;
import com.hao.topic.ai.mapper.AiAuditLogMapper;
import com.hao.topic.ai.mapper.AiHistoryMapper;
import com.hao.topic.ai.mapper.AiUserMapper;
import com.hao.topic.ai.properties.TtsProperties;
import com.hao.topic.ai.service.ModelService;
import com.hao.topic.api.utils.enums.StatusEnums;
import com.hao.topic.client.system.SystemFeignClient;
import com.hao.topic.client.topic.TopicFeignClient;
import com.hao.topic.common.enums.ResultCodeEnum;
import com.hao.topic.common.exception.TopicException;
import com.hao.topic.common.security.utils.SecurityUtils;
import com.hao.topic.model.dto.ai.AiHistoryDto;
import com.hao.topic.model.dto.ai.ChatDto;
import com.hao.topic.model.dto.topic.TopicAuditCategory;
import com.hao.topic.model.dto.topic.TopicAuditSubject;
import com.hao.topic.model.entity.ai.AiAuditLog;
import com.hao.topic.model.entity.ai.AiHistory;
import com.hao.topic.model.entity.ai.AiUser;
import com.hao.topic.model.entity.system.SysRole;
import com.hao.topic.model.entity.topic.TopicCategory;
import com.hao.topic.model.entity.topic.TopicSubject;
import com.hao.topic.model.vo.ai.AiHistoryContent;
import com.hao.topic.model.vo.ai.AiHistoryListVo;
import com.hao.topic.model.vo.ai.AiHistoryVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/20 14:45
 */
@Service
@Slf4j
public class ModelServiceImpl implements ModelService {
    private final ChatClient chatClient;
    @Autowired
    private AiHistoryMapper aiHistoryMapper;

    @Autowired
    private AiUserMapper aiUserMapper;

    @Autowired
    private TtsProperties ttsProperties;

    @Autowired
    private SystemFeignClient systemFeignClient;

    @Autowired
    private TopicFeignClient topicFeignClient;

    @Autowired
    private AiAuditLogMapper aiAuditLogMapper;

    public ModelServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    /**
     * 使用api发起对话
     *
     * @param chatDto
     * @return
     */
    public Flux<String> chat(ChatDto chatDto) {
        // 获取当前用户Id
        Long currentId = SecurityUtils.getCurrentId();
        // 当前账户
        String currentName = SecurityUtils.getCurrentName();
        // 判断角色
        String role = SecurityUtils.getCurrentRole();
        // 根据当前用户id和账户查询数据
        LambdaQueryWrapper<AiUser> aiUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        aiUserLambdaQueryWrapper.eq(AiUser::getUserId, currentId);
        aiUserLambdaQueryWrapper.eq(AiUser::getAccount, currentName);
        AiUser aiUser = aiUserMapper.selectOne(aiUserLambdaQueryWrapper);
        if (aiUser == null) {
            // 为空添加一条
            aiUser = new AiUser();
            aiUser.setUserId(currentId);
            aiUser.setAccount(currentName);
            aiUser.setAiCount(1L);
            // 根据角色标识查询角色
            SysRole byRoleKey = systemFeignClient.getByRoleKey(role);
            if (byRoleKey == null) {
                throw new TopicException(ResultCodeEnum.ROLE_NO_EXIST);
            }
            aiUser.setRoleName(byRoleKey.getName());
            // 设置最近使用时间
            aiUser.setRecentlyUsedTime(DateUtils.parseLocalDateTime(DateUtils.format(new Date())));
            aiUserMapper.insert(aiUser);
        } else {
            if (!aiUser.getRoleName().equals("管理员") && !aiUser.getRoleName().equals("会员")) {
                // 不为空校验是否还有次数
                if (aiUser.getAiCount() >= aiUser.getCount()) {
                    throw new TopicException(ResultCodeEnum.AI_COUNT_ERROR);
                }
            }
            // 是否被管理员停用了
            if (aiUser.getStatus() == 1) {
                throw new TopicException(ResultCodeEnum.AI_ERROR);
            }
            // 都正常
            // 使用次数+1
            aiUser.setAiCount(aiUser.getAiCount() + 1);
            // 更新最近使用时间
            aiUser.setRecentlyUsedTime(DateUtils.parseLocalDateTime(DateUtils.format(new Date())));
            aiUserMapper.updateById(aiUser);
        }

        // 判断用户状态
        // 校验模式
        if (chatDto.getModel().equals(AiConstant.SYSTEM_MODEL)) {
            // 系统模式
            return systemModel(chatDto);

        } else if (chatDto.getModel().equals(AiConstant.AI_MODEL)) {
            // AI模式
        }
        // 混合模式
        return this.chatClient.prompt()
                .user(chatDto.getPrompt())
                .stream()
                .content();
    }


    /**
     * 系统模式
     *
     * @param chatDto
     * @return
     */
    private Flux<String> systemModel(ChatDto chatDto) {
        // 封装返回数据
        AiHistory aiHistory = new AiHistory();
        // 获取当前用户Id
        Long currentId = SecurityUtils.getCurrentId();
        // 当前账户
        String currentName = SecurityUtils.getCurrentName();
        // 提示词
        String prompt = null;
        // 判断是否为第一次
        if (chatDto.getMemoryId() == 1) {
            prompt = PromptConstant.INTRODUCTION + "用户输入：" + chatDto.getPrompt();
            aiHistory.setParent(1);
        } else {
            // 1开始 2回答 3继续 4回答 5继续 6回答 7继续
            if (chatDto.getMemoryId() >= PromptConstant.START_CONTINUE_MEMORY_ID && (chatDto.getMemoryId() - PromptConstant.START_CONTINUE_MEMORY_ID) % PromptConstant.CONTINUE_INTERVAL == 0) {
                // 奇数次memoryId（3, 5, 7, ...）需要输入继续
                if (!chatDto.getContent().equals("继续")) {
                    return Flux.just("请输入'继续'");
                }
            }
            // 分页
            Page<AiHistory> aiHistoryPage = new Page<>(1, 1);
            // 添加上一次对话记忆 查询上一条数据
            LambdaQueryWrapper<AiHistory> aiHistoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
            aiHistoryLambdaQueryWrapper.eq(AiHistory::getChatId, chatDto.getChatId());
            aiHistoryLambdaQueryWrapper.orderByDesc(AiHistory::getCreateTime);
            aiHistoryLambdaQueryWrapper.eq(AiHistory::getUserId, currentId);
            aiHistoryLambdaQueryWrapper.eq(AiHistory::getAccount, currentName);
            Page<AiHistory> aiHistoryPageDb = aiHistoryMapper.selectPage(aiHistoryPage, aiHistoryLambdaQueryWrapper);
            if (aiHistoryPageDb.getRecords().size() > 0) {
                AiHistory aiHistoryDb = aiHistoryPageDb.getRecords().get(0);
                prompt = "你提出面试题：" + aiHistoryDb.getContent()
                        + "用户回答：" + chatDto.getPrompt() + "  " + PromptConstant.EVALUATE
                        + "引导用户输入'继续'：你才继续生成题目！";
            }
        }
        // 拼接信息
        StringBuffer fullReply = new StringBuffer();

        Flux<String> content = this.chatClient.prompt()
                .user(prompt)
                .stream()
                .content();
        Flux<String> stringFlux = content.flatMap(response -> {
            fullReply.append(response);
            return Flux.just(response);
        }).doOnComplete(() -> {
            log.info("执行完成保存历史记录");
            // 获取当前对话id
            String chatId = chatDto.getChatId();
            aiHistory.setChatId(chatId);
            aiHistory.setAccount(currentName);
            aiHistory.setUserId(currentId);
            aiHistory.setContent(fullReply.toString());
            aiHistory.setTitle(chatDto.getPrompt());
            // TODO 分割到状态
            // aiHistory.setAccuracy();
            aiHistoryMapper.insert(aiHistory);
        });
        return stringFlux;
    }


    /**
     * 获取历史记录
     *
     * @param aiHistoryDto
     * @return
     */
    @Transactional
    public List<AiHistoryListVo> getHistory(AiHistoryDto aiHistoryDto) {
        // 获取当前用户id
        Long currentId = SecurityUtils.getCurrentId();
        if (currentId == null) {
            throw new TopicException(ResultCodeEnum.AI_HISTORY_ERROR);
        }
        // 设置分页参数
        Page<AiHistory> aiHistoryPage = new Page<>(aiHistoryDto.getPageNum(), aiHistoryDto.getPageSize());
        // 设置分页条件
        LambdaQueryWrapper<AiHistory> aiHistoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        aiHistoryLambdaQueryWrapper.eq(AiHistory::getUserId, currentId); // 设置用户id
        aiHistoryLambdaQueryWrapper.eq(AiHistory::getParent, 1); // 表示第一条数据
        aiHistoryLambdaQueryWrapper.orderByDesc(AiHistory::getCreateTime);
        // 判断title是否存在
        if (StringUtils.isNotBlank(aiHistoryDto.getTitle())) {
            aiHistoryLambdaQueryWrapper.like(AiHistory::getTitle, aiHistoryDto.getTitle());
        }
        // 开始查询
        aiHistoryMapper.selectPage(aiHistoryPage, aiHistoryLambdaQueryWrapper);
        // 解析数据
        List<AiHistory> records = aiHistoryPage.getRecords();
        // 获取当天日期
        String today = DateUtils.format(new Date(), "yyyy-MM-dd");
        // 封装当天数据
        AiHistoryListVo aiHistoryListVo = new AiHistoryListVo();
        // 筛选当天数据
        List<AiHistoryVo> list = records.stream().filter(aiHistory -> {
            // 获取当前记录的创建时间
            String createTime = DateUtils.format(aiHistory.getCreateTime(), "yyyy-MM-dd");
            return createTime.equals(today);
        }).map(aiHistory -> {
            AiHistoryVo aiHistoryVo = new AiHistoryVo();
            BeanUtils.copyProperties(aiHistory, aiHistoryVo);
            return aiHistoryVo;
        }).toList();
        if (CollectionUtils.isNotEmpty(list)) {
            aiHistoryListVo.setAiHistoryVos(list);
            aiHistoryListVo.setDate(AiConstant.DAY_HISTORY);
        }
        // 封装其他日期的数据
        AiHistoryListVo aiHistoryListVoAll = new AiHistoryListVo();
        // 开始封装
        List<AiHistoryVo> otherRecords = records.stream().filter(aiHistory -> {
            // 获取当前记录的创建时间
            String createTime = DateUtils.format(aiHistory.getCreateTime(), "yyyy-MM-dd");
            return !createTime.equals(today);
        }).map(aiHistory -> {
            aiHistoryListVoAll.setDate(DateUtils.format(aiHistory.getCreateTime(), "yyyy-MM-dd"));
            AiHistoryVo aiHistoryVo = new AiHistoryVo();
            BeanUtils.copyProperties(aiHistory, aiHistoryVo);
            return aiHistoryVo;
        }).toList();
        if (!otherRecords.isEmpty()) {
            aiHistoryListVoAll.setAiHistoryVos(otherRecords);
            // 合并到一起
            return List.of(aiHistoryListVo, aiHistoryListVoAll);
        } else {
            return List.of(aiHistoryListVo);
        }
    }

    /**
     * 根据记录id获取到对话历史记录
     *
     * @param id
     * @return
     */
    public List<AiHistoryContent> getHistoryById(Long id) {
        // 校验
        if (id == null) {
            throw new TopicException(ResultCodeEnum.AI_HISTORY_ERROR);
        }
        // 查询当前父级
        AiHistory aiHistory = aiHistoryMapper.selectById(id);
        // 获取到对话id
        String chatId = aiHistory.getChatId();
        // 根据对话id查询所有的历史记录
        LambdaQueryWrapper<AiHistory> aiHistoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        aiHistoryLambdaQueryWrapper.eq(AiHistory::getChatId, chatId);
        aiHistoryLambdaQueryWrapper.eq(AiHistory::getUserId, SecurityUtils.getCurrentId());
        aiHistoryLambdaQueryWrapper.orderByDesc(AiHistory::getParent);
        aiHistoryLambdaQueryWrapper.orderByAsc(AiHistory::getCreateTime);
        List<AiHistory> aiHistories = aiHistoryMapper.selectList(aiHistoryLambdaQueryWrapper);
        // 封装返回数据
        return aiHistories.stream().map(history -> {
            AiHistoryContent aiHistoryContent = new AiHistoryContent();
            BeanUtils.copyProperties(history, aiHistoryContent);
            return aiHistoryContent;
        }).toList();
    }

    /**
     * 同步语言合成技术
     *
     * @param text
     * @return
     */
    public ResponseEntity<byte[]> tts(String text) {
        SpeechSynthesisParam param =
                SpeechSynthesisParam.builder()
                        .apiKey(ttsProperties.getApiKey())
                        .model(ttsProperties.getModel())
                        .voice(ttsProperties.getVoice())
                        .build();
        SpeechSynthesizer synthesizer = new SpeechSynthesizer(param, null);
        ByteBuffer audio = synthesizer.call(text); // 用前端传入的text

        byte[] audioBytes = audio.array();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=output.mp3");
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(audioBytes);
    }


    /**
     * 根据记录id删除对话记录
     *
     * @param id
     */
    public void deleteHistory(Long id) {
        // 校验id
        if (id == null) {
            throw new TopicException(ResultCodeEnum.AI_HISTORY_DELETE_ERROR);
        }
        // 查询当前记录
        AiHistory aiHistory = aiHistoryMapper.selectById(id);
        if (aiHistory == null) {
            throw new TopicException(ResultCodeEnum.AI_HISTORY_DELETE_ERROR);
        }
        // 根据对话id删除所有的历史记录
        LambdaQueryWrapper<AiHistory> aiHistoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        aiHistoryLambdaQueryWrapper.eq(AiHistory::getChatId, aiHistory.getChatId());
        // 物理删除
        aiHistoryMapper.delete(aiHistoryLambdaQueryWrapper);
    }


    /**
     * 根据记录id重命名标题
     *
     * @param aiHistoryDto
     */
    @Override
    public void updateHistoryById(AiHistoryDto aiHistoryDto) {
        if (aiHistoryDto == null) {
            throw new TopicException(ResultCodeEnum.AI_HISTORY_UPDATE_ERROR);
        }
        // 校验
        if (aiHistoryDto.getId() == null || aiHistoryDto.getTitle() == null) {
            throw new TopicException(ResultCodeEnum.AI_HISTORY_UPDATE_ERROR);
        }
        AiHistory aiHistory = aiHistoryMapper.selectById(aiHistoryDto.getId());
        if (aiHistory == null) {
            throw new TopicException(ResultCodeEnum.AI_HISTORY_UPDATE_ERROR);
        }
        // 开始修改
        aiHistory.setTitle(aiHistoryDto.getTitle());
        aiHistoryMapper.updateById(aiHistory);
    }

    /**
     * 审核专题
     *
     * @param topicAuditSubject
     */
    public void auditSubject(TopicAuditSubject topicAuditSubject) {
        // 获取分类
        String categoryName = topicAuditSubject.getCategoryName();
        // 获取专题名称
        String subjectName = topicAuditSubject.getSubjectName();
        // 专题描述
        String subjectDesc = topicAuditSubject.getSubjectDesc();
        // 提示词
        String prompt = PromptConstant.AUDIT_SUBJECT + "\n" +
                "专题内容: 【" + subjectName + "】\n" +
                "专题描述: 【" + subjectDesc + "】\n" +
                "分类名称: 【" + categoryName + "】";
        // 发送给ai
        String content = getAiContent(prompt, topicAuditSubject.getAccount(), topicAuditSubject.getUserId());
        // 解析结果
        log.info("AI返回结果: {}", content);
        TopicSubject topicSubject = new TopicSubject();
        StringBuilder resultContent = new StringBuilder();
        try {
            // 转换结果
            JSONObject jsonObject = JSON.parseObject(content);
            boolean result = false;
            if (jsonObject != null) {
                result = jsonObject.getBooleanValue("result");
            }
            String reason = null;
            if (jsonObject != null) {
                reason = jsonObject.getString("reason");
            }
            topicSubject.setId(topicAuditSubject.getId());
            if (result) {
                log.info("审核通过: {}", reason);
                resultContent.append(resultContent.append(reason));
                // 处理审核通过的逻辑
                topicSubject.setStatus(StatusEnums.NORMAL.getCode());
            } else {
                log.warn("审核未通过: {}", reason);
                resultContent.append(resultContent.append(reason));
                // 处理审核未通过的逻辑
                // 失败原因
                topicSubject.setFailMsg(reason);
                topicSubject.setStatus(StatusEnums.AUDIT_FAIL.getCode());
            }
        } catch (Exception e) {
            log.error("解析AI返回结果失败: {}", content, e);
            // 处理解析失败的情况
            topicSubject.setStatus(StatusEnums.AUDIT_FAIL.getCode());
            topicSubject.setFailMsg("解析AI返回结果失败");
            resultContent.append(resultContent.append("解析AI返回结果失败"));
        }
        // 调用远程服务的接口实现状态修改
        topicFeignClient.auditSubject(topicSubject);
        // 记录日志
        recordAuditLog(resultContent.toString(), topicAuditSubject.getAccount(), topicAuditSubject.getUserId());
    }

    /**
     * 审核分类名称是否合法
     *
     * @param topicAuditCategory
     */
    public void auditCategory(TopicAuditCategory topicAuditCategory) {
        // 获取分类名称
        String categoryName = topicAuditCategory.getCategoryName();
        // 封装提示词
        String prompt = PromptConstant.AUDIT_CATEGORY + "\n" +
                "分类名称: 【" + categoryName + "】";
        // 发送给ai
        String content = getAiContent(prompt, topicAuditCategory.getAccount(), topicAuditCategory.getUserId());
        // 解析结果
        log.info("AI返回结果: {}", content);
        TopicCategory topicCategory = new TopicCategory();
        StringBuilder resultContent = new StringBuilder();
        try {
            // 转换结果
            JSONObject jsonObject = JSON.parseObject(content);
            boolean result = false;
            if (jsonObject != null) {
                result = jsonObject.getBooleanValue("result");
            }
            String reason = null;
            if (jsonObject != null) {
                reason = jsonObject.getString("reason");
            }
            topicCategory.setId(topicAuditCategory.getId());
            if (result) {
                log.info("审核通过: {}", reason);
                resultContent.append(resultContent.append(reason));
                // 处理审核通过的逻辑
                topicCategory.setStatus(StatusEnums.NORMAL.getCode());
            } else {
                log.warn("审核未通过: {}", reason);
                resultContent.append(resultContent.append(reason));
                // 处理审核未通过的逻辑
                // 失败原因
                topicCategory.setFailMsg(reason);
                topicCategory.setStatus(StatusEnums.AUDIT_FAIL.getCode());
            }
        } catch (Exception e) {
            log.error("解析AI返回结果失败: {}", content, e);
            // 处理解析失败的情况
            topicCategory.setStatus(StatusEnums.AUDIT_FAIL.getCode());
            topicCategory.setFailMsg("解析AI返回结果失败");
            resultContent.append(resultContent.append("解析AI返回结果失败"));
        }
        // 调用远程服务的接口实现状态修改
        topicFeignClient.auditCategory(topicCategory);
        // 记录日志
        recordAuditLog(resultContent.toString(), topicAuditCategory.getAccount(), topicAuditCategory.getUserId());
    }

    /**
     * 获取ai返回的内容同步返回
     */
    public String getAiContent(String prompt, String account, Long userId) {
        try {
            // 发送给ai
            return this.chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();
        } catch (Exception e) {
            // 记录日志
            AiAuditLog aiAuditLog = new AiAuditLog();
            aiAuditLog.setAccount(account);
            aiAuditLog.setContent("AI回复异常");
            aiAuditLog.setUserId(userId);
            aiAuditLogMapper.insert(aiAuditLog);
            throw new TopicException(ResultCodeEnum.FAIL);
        }
    }

    /**
     * 记录审核的日志
     */
    public void recordAuditLog(String content, String account, Long userId) {
        AiAuditLog aiAuditLog = new AiAuditLog();
        aiAuditLog.setAccount(account);
        aiAuditLog.setContent(content);
        aiAuditLog.setUserId(userId);
        aiAuditLogMapper.insert(aiAuditLog);
    }
}
