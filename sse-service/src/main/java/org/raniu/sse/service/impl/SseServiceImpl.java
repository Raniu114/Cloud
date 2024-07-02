package org.raniu.sse.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.raniu.api.vo.Result;
import org.raniu.common.utils.RedisUtil;
import org.raniu.sse.redisListener.RedisHashListener;
import org.raniu.sse.service.SseService;
import org.raniu.sse.utils.SseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.sse.service.impl
 * @className: SseServiceImpl
 * @author: Raniu
 * @description: TODO
 * @date: 2024/7/1 14:02
 * @version: 1.0
 */

@Service
public class SseServiceImpl implements SseService {

    @Autowired
    private SseClient sseClient;

    @Override
    public SseEmitter createSse(String uid, String[] devices, HttpServletResponse response) {
        return sseClient.createSseEmitter(uid, devices);
    }

    @Override
    public Result<String> closeSse(String uid, HttpServletResponse response) {
        sseClient.closeSseEmitter(uid);
        return Result.success("删除成功");
    }
}
