package org.raniu.android.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.raniu.android.service.AndroidUpdateService;
import org.raniu.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.android.controller
 * @className: AndroidUpdateController
 * @author: Raniu
 * @description: TODO
 * @date: 2024/6/26 15:16
 * @version: 1.0
 */

@CrossOrigin
@RestController
@RequestMapping("/android")
@Tag(name = "Android更新接口", description = "Android更新接口")
public class AndroidUpdateController {

    @Autowired
    private AndroidUpdateService androidUpdateService;

    @GetMapping("/version/{name}")
    public Result<String> getVersion(@PathVariable(name = "name") String name, HttpServletRequest request, HttpServletResponse response) {
        return this.androidUpdateService.getVersion(name, response);
    }

    @GetMapping("/apk/{name}")
    public Result<String> getApk(@PathVariable(name = "name") String name, @RequestParam(name = "version") String version, HttpServletRequest request, HttpServletResponse response) {
        return this.androidUpdateService.getApk(name, version, response);
    }
}
