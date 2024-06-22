package org.raniu.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.raniu.api.vo.Result;
import org.raniu.project.domain.po.ProjectPo;
import org.raniu.project.domain.po.ProjectTypePo;
import org.raniu.project.domain.vo.ProjectTypeVo;
import org.raniu.project.domain.vo.ProjectVo;
import org.raniu.project.service.ProjectService;
import org.raniu.project.service.ProjectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.project.controller
 * @className: ProjectTypeController
 * @author: Raniu
 * @description: TODO
 * @date: 2024/6/17 17:01
 * @version: 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/project_type")
@Tag(name = "项目类型接口", description = "项目类型管理")
public class ProjectTypeController {
    @Autowired
    private ProjectTypeService projectTypeService;

    @PostMapping("/add")
    @Operation(summary = "添加项目类型", description = "添加项目类型")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    public Result<ProjectTypePo> addProjectType(@RequestBody @Valid ProjectTypeVo projectTypeVo, HttpServletRequest request, HttpServletResponse response) {
        return this.projectTypeService.addProjectType(projectTypeVo, response);
    }

    @PostMapping("/update")
    @Operation(summary = "更新项目类型信息", description = "更新项目类型信息")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    public Result<ProjectTypePo> updateProjectType(@RequestBody @Valid ProjectTypeVo projectTypeVo, HttpServletRequest request, HttpServletResponse response) {
        return this.projectTypeService.updateProjectType(projectTypeVo, response);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除项目类型", description = "删除项目类型")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    @Parameter(name = "id", description = "项目id")
    public Result<ProjectTypePo> deleteProjectType(@RequestParam(name = "id") String id, HttpServletResponse response, HttpServletRequest request) {
        return this.projectTypeService.deleteProjectType(id, response);
    }

    @GetMapping("/list")
    @Operation(summary = "项目类型列表", description = "项目类型列表")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    @Parameters({
            @Parameter(name = "page", description = "页码"),
            @Parameter(name = "size", description = "最大条目数")
    })
    public Result<List<ProjectTypePo>> projectListType(@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size, HttpServletRequest request, HttpServletResponse response) {
        return this.projectTypeService.listProjectType(page, size, response);
    }

    @GetMapping("/get")
    @Operation(summary = "获取项目类型", description = "获取单个项目类型信息")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    @Parameter(name = "id", description = "项目id")
    public Result<ProjectTypePo> getProjectType(@RequestParam(name = "id") String id, HttpServletResponse response, HttpServletRequest request) {
        return this.projectTypeService.getProjectType(id, response);
    }

    @GetMapping("/select")
    @Operation(summary = "查询项目类型", description = "查询项目类型")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    @ApiResponse(responseCode = "412", description = "id可能重复")
    @ApiResponse(responseCode = "200", description = "OK")
    @Parameters({
            @Parameter(name = "key", description = "关键词"),
            @Parameter(name = "page", description = "页码"),
            @Parameter(name = "size", description = "最大条目数")
    })
    public Result<List<ProjectTypePo>> selectProjectType(@RequestParam(name = "key") String key, @RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size, HttpServletResponse response, HttpServletRequest request) {
        return this.projectTypeService.selectProjectType(key, page, size, response);
    }
}
