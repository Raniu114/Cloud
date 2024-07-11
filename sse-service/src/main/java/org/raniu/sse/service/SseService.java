package org.raniu.sse.service;

import jakarta.servlet.http.HttpServletResponse;
import org.raniu.api.vo.Result;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.sse.service
 * @className: SseService
 * @author: Raniu
 * @description: TODO
 * @date: 2024/7/1 14:02
 * @version: 1.0
 */
public interface SseService {
    SseEmitter createSse(String uid, List<String> devices, HttpServletResponse response);

    Result<String> closeSse(String uid, HttpServletResponse response);
}
