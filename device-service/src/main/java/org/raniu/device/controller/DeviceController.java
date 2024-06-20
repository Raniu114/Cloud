package org.raniu.device.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import org.raniu.api.dto.DeviceDTO;
import org.raniu.api.vo.Result;
import org.raniu.device.domain.vo.DeviceVo;
import org.raniu.device.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

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
@RequestMapping("/device")
@Tag(name = "设备接口", description = "设备接口")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;


    @PostMapping("/add")
    @Operation(summary = "添加设备", description = "添加设备")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "未携带token"),
            @ApiResponse(responseCode = "403", description = "权限不足"),
            @ApiResponse(responseCode = "412", description = "id可能重复")
    })
    public Result<DeviceDTO> addDevice(@RequestBody DeviceVo deviceVo, HttpServletRequest request, HttpServletResponse response) {
        return this.deviceService.addDevice(deviceVo, response);
    }

    @PostMapping("/update")
    @Operation(summary = "更新设备信息", description = "更新设备信息")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "未携带token"),
            @ApiResponse(responseCode = "403", description = "权限不足"),
            @ApiResponse(responseCode = "412", description = "找不到设备")
    })
    public Result<DeviceDTO> updateDevice(@RequestBody DeviceVo deviceVo, HttpServletRequest request, HttpServletResponse response) {
        return this.deviceService.updateDevice(deviceVo, response);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除设备", description = "删除设备")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "未携带token"),
            @ApiResponse(responseCode = "403", description = "权限不足"),
            @ApiResponse(responseCode = "412", description = "参数缺失或找不到设备")
    })
    public Result<DeviceDTO> deleteDevice(@RequestParam(name = "id") String id, HttpServletResponse response, HttpServletRequest request) {
        return this.deviceService.deleteDevice(id, response);
    }

    @GetMapping("/list")
    @Operation(summary = "设备列表", description = "设备列表,如果为管理员用户则查询全部设备，如果为普通用户则查询名下设备")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "未携带token"),
            @ApiResponse(responseCode = "403", description = "权限不足"),
            @ApiResponse(responseCode = "412", description = "参数缺失或没有设备")
    })
    @Parameters({
            @Parameter(name = "page", description = "分页页码", required = true),
            @Parameter(name = "size", description = "单页长度", required = true),
    })
    public Result<List<DeviceDTO>> deviceList(@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size, HttpServletRequest request, HttpServletResponse response) {
        return this.deviceService.deviceList(page, size, response);
    }

    @GetMapping("/get")
    @Operation(summary = "获取设备", description = "获取单个设备")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "未携带token"),
            @ApiResponse(responseCode = "403", description = "权限与操作不符"),
            @ApiResponse(responseCode = "412", description = "参数缺失或找不到设备")
    })
    public Result<DeviceDTO> getDevice(@RequestParam(name = "id") String id, HttpServletResponse response, HttpServletRequest request) {
        return this.deviceService.getDevice(id,response);
    }

    @GetMapping("/select")
    @Operation(summary = "查询设备", description = "查询设备,可以提供名称关键词、协议关键词、所有者关键词进行查询")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "未携带token"),
            @ApiResponse(responseCode = "403", description = "权限不足"),
            @ApiResponse(responseCode = "412", description = "参数缺失或找不到用户")
    })
    public Result<List<DeviceDTO>> selectDevice(@RequestParam(name = "keys") List<String> keys,@RequestParam(name = "values") List<String> values, @RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size, HttpServletResponse response, HttpServletRequest request) {
        return this.deviceService.selectDevice(keys,values,page,size,response);
    }

//    @GetMapping("/values/{id}")
//    @ApiOperation(value = "获取设备下数据值", notes = "获取设备下数据值")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "设备id", required = true, dataType = "String", paramType = "path")
//    })
//    @ApiResponses({
//            @ApiResponse(code = 401, message = "未携带token"),
//            @ApiResponse(code = 403, message = "权限不足"),
//            @ApiResponse(code = 412, message = "参数缺失或找不到设备")
//    })
//    public String getValues(@PathVariable String id, HttpServletResponse response, HttpServletRequest request) {
//        JSONObject jsonObject = new JSONObject();
//        if (id == null) {
//            response.setStatus(412);
//            jsonObject.put("status", -1);
//            jsonObject.put("msg", "参数不可为空");
//            return jsonObject.toString();
//        }
//        int permissions = (int) request.getAttribute("permissions");
//        Long user = (Long) request.getAttribute("user");
//        if (!user.equals(this.projectService.getById(this.deviceService.getById(id).getOwner()).getOwner()) && permissions < 2) {
//            response.setStatus(403);
//            jsonObject.put("status", 0);
//            jsonObject.put("msg", "权限不足");
//            return jsonObject.toString();
//        }
//        Map<Object, Object> map = redisUtil.getHashEntries(id);
//        if (map.isEmpty()) {
//            response.setStatus(412);
//            jsonObject.put("status", -1);
//            jsonObject.put("msg", "找不到设备");
//            return jsonObject.toString();
//        }
//        jsonObject.put("status", 1);
//        jsonObject.put("msg", "获取成功");
//        jsonObject.put("values", map);
//        return jsonObject.toString();
//    }
}
