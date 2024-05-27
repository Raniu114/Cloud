package org.raniu.project.controller;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.json.JSONObject;

import org.raniu.common.utils.UserContext;
import org.raniu.project.domain.po.ProjectPo;
import org.raniu.project.domain.vo.ProjectVo;
import org.raniu.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
    public String addProject(@RequestBody @Valid ProjectVo projectVo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
        for (ObjectError error : bindingResult.getAllErrors()) {
            return error.getDefaultMessage();
        }
        JSONObject jsonObject = new JSONObject();
        cn.hutool.json.JSONObject authJson = JSONUtil.parseObj(UserContext.getAuth());
        if ("write".equals(authJson.get("project")) && UserContext.getPermissions() < 2) {
            response.setStatus(403);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "权限不足");
            return jsonObject.toString();
        }
        ProjectPo projectPo = new ProjectPo();
        projectPo.setId(projectVo.getId());
        projectPo.setName(projectVo.getName());
        projectPo.setOwner(projectVo.getOwner());
        if (!this.projectService.save(projectPo)) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "添加失败，请检查id是否重复");
            return jsonObject.toString();
        }
        jsonObject.put("status", 1);
        jsonObject.put("msg", "添加成功");
        return jsonObject.toString();
    }

    @PostMapping("/update")
    @Operation(summary = "更新项目信息", description = "更新项目信息")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    @ApiResponse(responseCode = "412", description = "id可能重复")
    public String updateProject(@RequestBody @Valid ProjectVo projectVo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
        for (ObjectError error : bindingResult.getAllErrors()) {
            return error.getDefaultMessage();
        }
        JSONObject jsonObject = new JSONObject();
        cn.hutool.json.JSONObject authJson = JSONUtil.parseObj(UserContext.getAuth());
        if ("write".equals(authJson.get("project")) && UserContext.getPermissions() < 2) {
            response.setStatus(403);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "权限不足");
            return jsonObject.toString();
        }
        ProjectPo projectPo = new ProjectPo();
        projectPo.setOwner(projectVo.getOwner());
        projectPo.setName(projectVo.getName());
        projectPo.setId(projectVo.getId());
        if (this.projectService.updateById(projectPo)) {
            jsonObject.put("status", 1);
            jsonObject.put("msg", "更新成功");
        } else {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "更新失败");
        }
        return jsonObject.toString();
    }

    @PostMapping("/delete")
    @Operation(summary = "删除项目", description = "删除项目")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    @ApiResponse(responseCode = "412", description = "id可能重复")
    @Parameter(name = "id", description = "项目id")
    public String deleteProject(@RequestParam(name = "id") String id, HttpServletResponse response, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        if (id == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "参数不可为空");
            return jsonObject.toString();
        }
        cn.hutool.json.JSONObject authJson = JSONUtil.parseObj(UserContext.getAuth());
        if ("write".equals(authJson.get("project")) && UserContext.getPermissions() < 2) {
            response.setStatus(403);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "权限不足");
            return jsonObject.toString();
        }
        if (this.projectService.removeById(id)) {
            jsonObject.put("status", 1);
            jsonObject.put("msg", "删除成功");
        } else {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "未找到项目");
        }
        return jsonObject.toString();
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
        JSONObject jsonObject = new JSONObject();
        if (page == null || size == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "参数不可为空");
            return jsonObject.toString();
        }
        cn.hutool.json.JSONObject authJson = JSONUtil.parseObj(UserContext.getAuth());
        if ("none".equals(authJson.get("project")) && UserContext.getPermissions() < 2) {
            response.setStatus(403);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "权限不足");
            return jsonObject.toString();
        }
        Page<ProjectPo> projects;
        if (UserContext.getPermissions() > 1) {
            projects = this.projectService.list(page, size);
        } else {
            projects = this.projectService.list(page, size, UserContext.getUser());
        }
        if (!projects.getRecords().isEmpty()) {
            jsonObject.put("status", 1);
            jsonObject.put("msg", "获取成功");
            jsonObject.put("projects", projects.getRecords());
            jsonObject.put("pages", projects.getPages());
        } else {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "获取失败");
        }
        return jsonObject.toString();
    }

    @GetMapping("/get")
    @Operation(summary = "获取项目", description = "获取单个项目")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    @ApiResponse(responseCode = "412", description = "id可能重复")
    @Parameter(name = "id", description = "项目id")
    public String getProject(@RequestParam(name = "id") String id, HttpServletResponse response, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        if (id == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "参数不可为空");
            return jsonObject.toString();
        }
        cn.hutool.json.JSONObject authJson = JSONUtil.parseObj(UserContext.getAuth());
        if ("none".equals(authJson.get("project")) && UserContext.getPermissions() < 2) {
            response.setStatus(403);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "权限不足");
            return jsonObject.toString();
        }
        ProjectPo projectPo = this.projectService.getById(id);
        if (projectPo == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "未查询到项目");
            return jsonObject.toString();
        }
        jsonObject.put("status", 1);
        jsonObject.put("msg", "获取成功");
        jsonObject.put("project", BeanMap.create(projectPo));
        return jsonObject.toString();
    }

    @GetMapping("/select")
    @Operation(summary = "查询项目", description = "查询项目,可以提供名称关键词、所有者关键词进行查询")
    @ApiResponse(responseCode = "401", description = "未携带token")
    @ApiResponse(responseCode = "403", description = "权限不足")
    @ApiResponse(responseCode = "412", description = "id可能重复")
    @Parameters({
            @Parameter(name = "key", description = "关键词"),
            @Parameter(name = "page", description = "页码"),
            @Parameter(name = "size", description = "最大条目数")
    })
    public String selectProject(@RequestParam(name = "key") String key, @RequestParam(name = "page") String page, @RequestParam(name = "size") String size, HttpServletResponse response, HttpServletRequest request) {
        return this.projectService.selectProject(key, page, size, response);
    }
}
