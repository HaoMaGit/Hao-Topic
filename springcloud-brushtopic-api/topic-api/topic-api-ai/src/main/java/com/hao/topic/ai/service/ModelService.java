package com.hao.topic.ai.service;

import com.hao.topic.model.dto.ai.ChatDto;
import reactor.core.publisher.Flux;

/**
 * Description:
 * Author: Hao
 * Date: 2025/4/20 14:45
 */
public interface ModelService {


    Flux<String> chat(ChatDto chatDto);

}
