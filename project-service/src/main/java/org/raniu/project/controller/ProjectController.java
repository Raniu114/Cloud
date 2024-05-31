package org.raniu.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.raniu.project.domain.vo.ProjectVo;
import org.raniu.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
@RequestMapping("/project")
@Tag(name = "项目接口", description = "项目管理")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/add")
    @Operation(summary = "添加项目", description = "添加项目")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    @ApiResponse(responseCode = "412", description = "id可能重复")
    public String addProject(@RequestBody @Valid ProjectVo projectVo, HttpServletRequest request, HttpServletResponse response) {
        return this.projectService.addProject(projectVo, response);
    }

    @PostMapping("/update")
    @Operation(summary = "更新项目信息", description = "更新项目信息")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    @ApiResponse(responseCode = "412", description = "id可能重复")
    public String updateProject(@RequestBody @Valid ProjectVo projectVo, HttpServletRequest request, HttpServletResponse response) {
        return this.projectService.updateProject(projectVo, response);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除项目", description = "删除项目")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    @ApiResponse(responseCode = "412", description = "id可能重复")
    @Parameter(name = "id", description = "项目id")
    public String deleteProject(@RequestParam(name = "id") String id, HttpServletResponse response, HttpServletRequest request) {
        return this.projectService.deleteProject(id, response);
    }

    @GetMapping("/list")
    @Operation(summary = "项目列表", description = "项目列表,如果为管理员用户则查询全部项目，如果为普通用户则查询名下项目")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    @ApiResponse(responseCode = "412", description = "id可能重复")
    @Parameters({
            @Parameter(name = "page", description = "页码"),
            @Parameter(name = "size", description = "最大条目数")
    })
    public String projectList(@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size, HttpServletRequest request, HttpServletResponse response) {
        return this.projectService.listProject(page, size, response);
    }

    @GetMapping("/get")
    @Operation(summary = "获取项目", description = "获取单个项目")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    @ApiResponse(responseCode = "412", description = "id可能重复")
    @Parameter(name = "id", description = "项目id")
    public String getProject(@RequestParam(name = "id") String id, HttpServletResponse response, HttpServletRequest request) {
        return this.projectService.getProject(id, response);
    }

    @GetMapping("/select")
    @Operation(summary = "查询项目", description = "查询项目,可以提供名称关键词、所有者关键词进行查询")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")    
    @ApiResponse(responseCode = "412", description = "id可能重复")
    @ApiResponse(responseCode = "200", description = "OK")
    @Parameters({
            @Parameter(name = "key", description = "关键词"),
            @Parameter(name = "page", description = "页码"),
            @Parameter(name = "size", description = "最大条目数")
    })
    public String selectProject(@RequestParam(name = "key") String key, @RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size, HttpServletResponse response, HttpServletRequest request) {
        return this.projectService.selectProject(key, page, size, response);
    }
}
