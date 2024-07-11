package org.raniu.sse.utils;

import lombok.Data;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.ssecommon
 * @className: Sse
 * @author: Raniu
 * @description: TODO
 * @date: 2024/7/1 13:43
 * @version: 1.0
 */

@Data
public class Sse {

    private List<String> devices;
    private SseEmitter sseEmitter;
}
