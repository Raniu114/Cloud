package org.raniu.sse.redisListener;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NonNull;
import org.raniu.common.utils.RedisUtil;
import org.raniu.sse.utils.SseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.KeyspaceEventMessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.sse.utils
 * @className: RedisHashListener
 * @author: Raniu
 * @description: TODO
 * @date: 2024/7/1 14:38
 * @version: 1.0
 */

@Component
public class RedisHashListener implements MessageListener{
    public static final Topic TOPIC = new PatternTopic("__keyevent@0__:*");

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SseClient sseClient;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String patters = new String(message.getChannel());
        String msg = new String(message.getBody());
        if (patters.contains("__keyevent@0__:hset")) {
            if (sseClient.subClients.containsKey(msg)) {
                Map<Object, Object> value = redisUtil.getHashEntries(msg);
                if (!StrUtil.isBlank(msg)){
                    sseClient.subClients.get(msg).removeIf(sseEmitter -> !sseClient.sendSseEmitter(sseEmitter, msg, value));
                }
                value.clear();
            } else {
                sseClient.subClients.put(msg, new ArrayList<String>());
            }
        } else if (patters.contains("__keyevent@0__:del")) {
            if (sseClient.subClients.containsKey(msg)) {
                Map<Object, Object> map = new HashMap<>();
                map.put("msg", "设备已下线");
                sseClient.subClients.get(msg).removeIf(sseEmitter -> !sseClient.sendSseEmitter(sseEmitter, msg, map));
            }
        }
    }


}
