package com.hao.ai.client.ai;

import com.hao.topic.common.auth.TokenInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/5 22:34
 */
@FeignClient(name = "service-ai", configuration = TokenInterceptor.class)
public interface AiFeignClient {

    /**
     * 根据日期查询ai使用总数
     */
    @GetMapping("/ai/model/count/{date}")
    Long countDate(@PathVariable String date);

    /**
     * 查询ai使用总数
     *
     * @return
     */
    @GetMapping("/ai/model/count")
    Long count();
}
