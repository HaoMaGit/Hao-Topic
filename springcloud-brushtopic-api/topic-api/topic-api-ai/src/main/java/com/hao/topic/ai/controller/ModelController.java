package com.hao.topic.ai.controller;

import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesisParam;
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesizer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hao.topic.ai.properties.TtsProperties;
import com.hao.topic.ai.service.ModelService;
import com.hao.topic.common.result.Result;
import com.hao.topic.model.dto.ai.AiHistoryDto;
import com.hao.topic.model.dto.ai.ChatDto;
import com.hao.topic.model.dto.ai.TtsDto;
import com.hao.topic.model.entity.system.SysUser;
import com.hao.topic.model.vo.ai.AiHistoryContent;
import com.hao.topic.model.vo.ai.AiHistoryListVo;
import com.hao.topic.model.vo.topic.TopicDataVo;
import com.hao.topic.security.utils.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: Ai模型控制器
 * Author: Hao
 * Date: 2025/4/18 22:30
 */
@RestController
@RequestMapping("ai/model")
@AllArgsConstructor
public class ModelController {
    private final ModelService modelService;

    /**
     * 流式对话
     *
     * @param chatDto
     * @return
     */
    @PostMapping("/chat")
    public Flux<String> chat(@RequestBody ChatDto chatDto) {
        return modelService.chat(chatDto);
    }

    /**
     * 获取历史记录
     *
     * @param aiHistoryDto
     * @return
     */
    @GetMapping("/history")
    public Result getHistory(AiHistoryDto aiHistoryDto) {
        List<AiHistoryListVo> historyListVoList = modelService.getHistory(aiHistoryDto);
        return Result.success(historyListVoList);
    }

    /**
     * 根据记录id获取当前对话的历史记录
     *
     * @param id
     * @return
     */
    @GetMapping("/history/{id}")
    public Result getHistoryById(@PathVariable Long id) {
        List<AiHistoryContent> aiHistoryContentList = modelService.getHistoryById(id);
        return Result.success(aiHistoryContentList);
    }


    /**
     * 语音合成
     */
    @PostMapping("tts")
    public ResponseEntity<byte[]> tts(@RequestBody TtsDto ttsDto) {
        return modelService.tts(ttsDto);
    }

    /**
     * 根据记录id删除对话记录
     *
     * @param id
     * @return
     */
    @DeleteMapping("/history/{id}")
    public Result deleteHistory(@PathVariable Long id) {
        modelService.deleteHistory(id);
        return Result.success();
    }

    /**
     * 根据记录id重命名标题
     *
     * @param aiHistoryDto
     * @return
     */
    @PutMapping("/history")
    public Result updateHistory(@RequestBody AiHistoryDto aiHistoryDto) {
        modelService.updateHistoryById(aiHistoryDto);
        return Result.success();
    }


    /**
     * 根据日期查询ai使用总数
     */
    @GetMapping("/count/{date}")
    public Long countDate(@PathVariable String date) {
        return modelService.count(date);
    }

    /**
     * 查询ai使用总数
     *
     * @return
     */
    @GetMapping("/count")
    public Long count() {
        return modelService.count(null);
    }

    /**
     * 查询近7日ai使用次数
     *
     * @return
     */
    @GetMapping("/countAiDay7")
    List<TopicDataVo> countAiDay7() {
        return modelService.countAiDay7();
    }
}
