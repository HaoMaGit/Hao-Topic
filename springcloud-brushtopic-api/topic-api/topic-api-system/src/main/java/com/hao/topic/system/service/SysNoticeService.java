package com.hao.topic.system.service;

import com.hao.topic.model.dto.system.SysNoticeDto;
import com.hao.topic.model.vo.system.SysNoticeVo;

import java.util.List;

/**
 * Description:
 * Author: Hao
 * Date: 2025/5/3 16:28
 */
public interface SysNoticeService {
    void recordNotice(SysNoticeDto sysNoticeDto);

    List<SysNoticeVo> list();
}
