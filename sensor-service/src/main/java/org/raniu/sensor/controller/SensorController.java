package org.raniu.sensor.controller;


import io.swagger.annotations.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.raniu.api.dto.SensorDTO;
import org.raniu.api.vo.Result;

import org.raniu.sensor.domain.vo.SensorVo;
import org.raniu.sensor.service.SensorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author raniu
 * @since 2023-12-11
 */
@CrossOrigin
@RestController
@RequestMapping("/sensor")
@Api(value = "传感器接口", tags = "传感器接口")
public class SensorController {

    @Autowired
    private SensorService sensorService;

    @PostMapping("/add")
    @ApiOperation(value = "添加传感器", notes = "添加传感器")
    @ApiResponses({
            @ApiResponse(code = 401, message = "未携带token"),
            @ApiResponse(code = 403, message = "权限不足"),
            @ApiResponse(code = 412, message = "id可能重复")
    })
    public Result<String> addSensor(@RequestBody SensorVo sensorVo, HttpServletRequest request, HttpServletResponse response) {
        return this.sensorService.addSensor(sensorVo, response);
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新传感器信息", notes = "更新传感器信息")
    @ApiResponses({
            @ApiResponse(code = 401, message = "未携带token"),
            @ApiResponse(code = 403, message = "权限不足"),
            @ApiResponse(code = 412, message = "找不到传感器")
    })
    public Result<String> updateSensor(@RequestBody SensorVo sensorVo, HttpServletRequest request, HttpServletResponse response) {
        return this.sensorService.updateSensor(sensorVo, response);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除传感器", notes = "删除传感器")
    @ApiResponses({
            @ApiResponse(code = 401, message = "未携带token"),
            @ApiResponse(code = 403, message = "权限不足"),
            @ApiResponse(code = 412, message = "参数缺失或找不到传感器")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "传感器id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deviceId", value = "设备id", required = true, dataType = "String", paramType = "query")
    })
    public Result<String> deleteSensor(@RequestParam("id") String id, @RequestParam("deviceId") String deviceId, HttpServletResponse response, HttpServletRequest request) {
        return this.sensorService.deleteSensor(id, deviceId, response);
    }

    @GetMapping("/list")
    @ApiOperation(value = "传感器列表", notes = "设备列表,查询设备下所有传感器")
    @ApiResponses({
            @ApiResponse(code = 401, message = "未携带token"),
            @ApiResponse(code = 403, message = "权限不足"),
            @ApiResponse(code = 412, message = "参数缺失或没有传感器")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页页码", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "单页长度", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "deviceId", value = "设备id", required = true, dataType = "String", paramType = "query")
    })
    public Result<List<SensorDTO>> sensorList(@RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestParam("deviceID") String deviceId, @RequestParam(name = "type",required = false) Integer type, HttpServletRequest request, HttpServletResponse response) {
        return this.sensorService.sensorList(page, size, deviceId, type, response);
    }

    @GetMapping("/get")
    @ApiOperation(value = "获取传感器", notes = "获取单个传感器")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "关键词", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deviceId", value = "设备id", required = true, dataType = "String", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 401, message = "未携带token"),
            @ApiResponse(code = 403, message = "权限与操作不符"),
            @ApiResponse(code = 412, message = "参数缺失或找不到传感器")
    })
    public Result<SensorDTO> getSensor(@RequestParam("id") String id, @RequestParam("deviceId") String deviceId, HttpServletResponse response, HttpServletRequest request) {
        return this.sensorService.getSensor(id, deviceId, response);
    }

    @GetMapping("/select")
    @ApiOperation(value = "查询传感器", notes = "查询项目,可以提供任意关键词进行查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页页码", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "单页长度", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "key", value = "关键词", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deviceId", value = "设备id", required = true, dataType = "String", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 401, message = "未携带token"),
            @ApiResponse(code = 403, message = "权限不足"),
            @ApiResponse(code = 412, message = "参数缺失或找不到传感器")
    })
    public Result<List<SensorDTO>> selectSensor(@RequestParam("key") String key, @RequestParam("deviceId") String deviceId, @RequestParam Integer page, @RequestParam Integer size, HttpServletResponse response, HttpServletRequest request) {
        return this.sensorService.selectSensor(key, deviceId, page, size, response);
    }


}

