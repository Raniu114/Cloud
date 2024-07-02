package org.raniu.sse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.raniu.api.vo.Result;
import org.raniu.sse.service.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.sse.controller
 * @className: SseController
 * @author: Raniu
 * @description: TODO
 * @date: 2024/7/1 13:56
 * @version: 1.0
 */

@CrossOrigin
@RestController
@RequestMapping("/sse")
public class SseController {

    @Autowired
    private SseService service;

    @GetMapping("/create")
    public SseEmitter createSse(@RequestParam("uid") String uid, @RequestParam("devices") String[] devices, HttpServletResponse response, HttpServletRequest request) {
        return this.service.createSse(uid, devices, response);
    }

    @PostMapping("/close")
    public Result<String> closeSse(@RequestParam("uid") String uid, HttpServletResponse response, HttpServletRequest request) {
        return this.service.closeSse(uid, response);
    }

}
