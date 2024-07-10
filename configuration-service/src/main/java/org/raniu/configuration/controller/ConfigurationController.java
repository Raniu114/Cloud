package org.raniu.configuration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.raniu.api.vo.Result;
import org.raniu.configuration.domain.po.ConfigurationPo;
import org.raniu.configuration.domain.vo.ConfigurationVo;
import org.raniu.configuration.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author raniu
 * @since 2024-01-10
 */
@CrossOrigin
@RestController
@RequestMapping("/configuration")
@Tag(name = "组态接口", description = "组态管理")
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;


    @GetMapping("/list")
    @Operation(summary = "组态列表", description = "组态列表,查询对应项目下的组态")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    @ApiResponse(responseCode = "412", description = "id可能重复")
    @Parameters({
            @Parameter(name = "page", description = "页码"),
            @Parameter(name = "size", description = "最大条目数"),
            @Parameter(name = "projectId", description = "项目id")
    })
    public Result<List<ConfigurationPo>> configurationList(@RequestParam(name = "projectId") String projectId,@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size ,HttpServletResponse response, HttpServletRequest request) {
        return configurationService.configurationList(projectId,page,size,response);
    }

    @PostMapping("/add")
    @Operation(summary = "添加组态", description = "添加组态")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    @ApiResponse(responseCode = "412", description = "id可能重复")
    public Result<ConfigurationPo> addConfiguration(@RequestBody ConfigurationVo configurationVo, HttpServletRequest request, HttpServletResponse response, BindingResult bindingResult) {
        return configurationService.addConfiguration(configurationVo,response);
    }

    @PostMapping("/update")
    @Operation(summary = "更新组态信息", description = "更新组态信息")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    @ApiResponse(responseCode = "412", description = "id可能重复")
    public Result<ConfigurationPo> updateConfiguration(@RequestParam("id") Long id,@RequestBody ConfigurationVo configurationVo, HttpServletRequest request, HttpServletResponse response, BindingResult bindingResult) {
        return configurationService.updateConfiguration(id,configurationVo,response);
    }

    @PostMapping("/del")
    @Operation(summary = "删除组态", description = "删除组态")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    @ApiResponse(responseCode = "412", description = "id可能重复")
    @Parameter(name = "id", description = "组态id")
    public Result<ConfigurationPo> delConfiguration(@RequestParam("id") Long id, HttpServletResponse response, HttpServletRequest request) {
        return configurationService.delConfiguration(id,response);
    }

    @PostMapping("/img/upload")
    @Operation(summary = "上传图片", description = "上传图片")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    @ApiResponse(responseCode = "412", description = "id可能重复")
    public Result<ConfigurationPo> updateImage(@RequestParam("id") String id, @RequestParam("file") MultipartFile multipartFile, HttpServletResponse response, HttpServletRequest request) {
        return configurationService.updateImage(id,multipartFile,response);
    }

    @GetMapping("/img/download/{id}")
    @Operation(summary = "下载图片", description = "下载图片")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    @ApiResponse(responseCode = "412", description = "id可能重复")
    public Result<ConfigurationPo> downloadImage(@PathVariable("id") String id, @RequestParam("img") String img, HttpServletRequest request, HttpServletResponse response) {
        return configurationService.downloadImage(id,img,response);
    }

    @GetMapping("/img/list/{id}")
    @Operation(summary = "图片列表", description = "图片列表")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    @ApiResponse(responseCode = "412", description = "id可能重复")
    public Result<String[]> imgList(@PathVariable("id") String id, HttpServletResponse response, HttpServletRequest request){
        return configurationService.imgList(id,response);
    }

    @PostMapping("/img/del/{id}")
    @Operation(summary = "删除图片", description = "删除图片")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    @ApiResponse(responseCode = "412", description = "id可能重复")
    public Result<ConfigurationPo> delImg(@RequestParam("img") String img,@PathVariable("id") String id, HttpServletResponse response, HttpServletRequest request){
        return configurationService.delImg(id,img,response);
    }
}

