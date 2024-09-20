package org.raniu.sse.utils;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.ssecommon.func
 * @className: Sse
 * @author: Raniu
 * @description: SSE 客户端逻辑实现类
 * @date: 2024/7/1 10:08
 * @version: 1.0
 */

@Component
public class SseClient {

    private static final Map<String, SseBean> sseEmitterMap = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(SseClient.class);
    public Map<String, List<String>> subClients = new HashMap<>();


    public SseEmitter createSseEmitter(String id, List<String> devices) {
        SseEmitter sseEmitter = new SseEmitter(0L);
        SseBean sseBean = new SseBean();
        sseBean.setSseEmitter(sseEmitter);
        sseBean.setDevices(devices);
        sseEmitter.onCompletion(() -> {
            sseEmitterMap.remove(id);
            for (String device : devices) {
                subClients.get(device).remove(id);
                if (subClients.get(device).isEmpty()) {
                    subClients.remove(device);
                }
            }
            log.info("sse close:{}", id);
        });
        sseEmitterMap.put(id, sseBean);
        for (String device : devices) {
            if (subClients.containsKey(device)) {
                subClients.get(device).add(id);
            } else {
                List<String> sseEmitterList = new ArrayList<>();
                sseEmitterList.add(id);
                subClients.put(device, sseEmitterList);
                Map<Object, Object> map = new HashMap<>();
                map.put("msg", "设备不在线");
                this.sendSseEmitter(id, device, map);
            }
        }
        return sseEmitter;
    }

    public void closeSseEmitter(String id) {
        if (sseEmitterMap.containsKey(id)) {
            sseEmitterMap.get(id).getSseEmitter().complete();
            sseEmitterMap.remove(id);
        }
    }

    public boolean sendSseEmitter(String id, String messageId, Map<Object,Object> message) {
        if (message.isEmpty()) {
            return false;
        }
        if (!sseEmitterMap.containsKey(id)) {
            return false;
        }
        SseEmitter sseEmitter = sseEmitterMap.get(id).getSseEmitter();
        if (sseEmitter == null) {
            sseEmitterMap.remove(id);
            return false;
        }
        try {
            JSONObject jsonObject = new JSONObject(message);
            sseEmitter.send(SseEmitter.event().id(messageId).reconnectTime(1 * 60 * 1000L).data(jsonObject.toString()));
            return true;
        } catch (IOException e) {
            sseEmitter.complete();
            sseEmitterMap.remove(id);
            return false;
        }
    }

}
