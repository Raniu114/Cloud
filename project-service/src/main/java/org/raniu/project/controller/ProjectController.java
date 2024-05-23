package org.raniu.project.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.json.JSONObject;

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
@Api(tags = "项目接口", value = "项目接口")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PutMapping("/add")
    @ApiOperation(value = "添加项目", notes = "添加项目")
    @ApiImplicitParam(name = "AccessToken", value = "AccessToken", required = true, dataType = "String", paramType = "header")
    @ApiResponses({
            @ApiResponse(code = 401, message = "未携带token"),
            @ApiResponse(code = 403, message = "权限不足"),
            @ApiResponse(code = 412, message = "id可能重复")
    })
    public String addProject(@RequestBody @Valid ProjectVo projectVo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
        for (ObjectError error : bindingResult.getAllErrors()) {
            return error.getDefaultMessage();
        }
        JSONObject jsonObject = new JSONObject();
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
    @ApiOperation(value = "更新项目信息", notes = "更新项目信息")
    @ApiImplicitParam(name = "AccessToken", value = "AccessToken", required = true, dataType = "String", paramType = "header")
    @ApiResponses({
            @ApiResponse(code = 401, message = "未携带token"),
            @ApiResponse(code = 403, message = "权限不足"),
            @ApiResponse(code = 412, message = "找不到项目")
    })
    public String updateProject(@RequestBody @Valid ProjectVo projectVo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
        for (ObjectError error : bindingResult.getAllErrors()) {
            return error.getDefaultMessage();
        }
        JSONObject jsonObject = new JSONObject();
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

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除项目", notes = "删除项目")
    @ApiResponses({
            @ApiResponse(code = 401, message = "未携带token"),
            @ApiResponse(code = 403, message = "权限不足"),
            @ApiResponse(code = 412, message = "参数缺失或找不到项目")
    })
    @ApiImplicitParam(name = "AccessToken", value = "AccessToken", required = true, dataType = "String", paramType = "header")
    public String deleteProject(@RequestParam String id, HttpServletResponse response, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        if (id == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "参数不可为空");
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
    @ApiOperation(value = "项目列表", notes = "项目列表,如果为管理员用户则查询全部项目，如果为普通用户则查询名下项目")
    @ApiResponses({
            @ApiResponse(code = 401, message = "未携带token"),
            @ApiResponse(code = 403, message = "权限不足"),
            @ApiResponse(code = 412, message = "参数缺失或没有项目")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "AccessToken", value = "AccessToken", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "page", value = "分页页码", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "单页长度", required = true, dataType = "Integer", paramType = "query"),
    })
    public String projectList(@RequestParam Integer page, @RequestParam Integer size, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        if (page == null || size == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "参数不可为空");
            return jsonObject.toString();
        }
        Page<ProjectPo> projects;
        int permissions = (int)request.getAttribute("permissions");
        Long user = (Long) request.getAttribute("user");
        if(permissions >1){
            projects = this.projectService.list(page, size);
        }else{
            projects = this.projectService.list(page, size, user);
        }
        if (!projects.getRecords().isEmpty()) {
            jsonObject.put("status", 1);
            jsonObject.put("msg", "获取成功");
            jsonObject.put("projects", projects.getRecords());
            jsonObject.put("pages",projects.getPages());
        } else {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "获取失败");
        }
        return jsonObject.toString();
    }

    @GetMapping("/get")
    @ApiOperation(value = "获取项目", notes = "获取单个项目")
    @ApiImplicitParam(name = "AccessToken", value = "AccessToken", required = true, dataType = "String", paramType = "header")
    @ApiResponses({
            @ApiResponse(code = 401, message = "未携带token"),
            @ApiResponse(code = 403, message = "权限与操作不符"),
            @ApiResponse(code = 412, message = "参数缺失或找不到项目")
    })
    public String getProject(@RequestParam String id, HttpServletResponse response, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        if (id == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "参数不可为空");
            return jsonObject.toString();
        }
        int permissions = (int)request.getAttribute("permissions");
        Long user = (Long) request.getAttribute("user");
        ProjectPo projectPo = this.projectService.getById(id);
        if (projectPo == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "未查询到项目");
            return jsonObject.toString();
        }
        if (!projectPo.getOwner().equals(user) && permissions < 2) {
            response.setStatus(403);
            jsonObject.put("status", 0);
            jsonObject.put("msg", "权限不足");
            return jsonObject.toString();
        }
        jsonObject.put("status", 1);
        jsonObject.put("msg", "获取成功");
        jsonObject.put("project", BeanMap.create(projectPo));
        return jsonObject.toString();
    }

    @GetMapping("/select")
    @ApiOperation(value = "查询项目", notes = "查询项目,可以提供名称关键词、所有者关键词进行查询")
    @ApiImplicitParam(name = "AccessToken", value = "AccessToken", required = true, dataType = "String", paramType = "header")
    @ApiResponses({
            @ApiResponse(code = 401, message = "未携带token"),
            @ApiResponse(code = 403, message = "权限不足"),
            @ApiResponse(code = 412, message = "参数缺失或找不到用户")
    })
    public String selectProject(@RequestHeader(name = "permissions") String permissions,@RequestHeader(name = "user") String user, @RequestParam(name = "key") String key,@RequestParam(name = "page") String page,@RequestParam(name = "size") String size,HttpServletResponse response,HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        if (key == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "参数不可为空");
            return jsonObject.toString();
        }

        Page<ProjectPo> projects;
        if (Integer.parseInt(permissions) > 1) {
            projects = this.projectService.select(key, Integer.parseInt(page), Integer.parseInt(size));
        }else {
            projects = this.projectService.select(key,Long.parseLong(user),Integer.parseInt(page), Integer.parseInt(size));
        }
        jsonObject.put("status",1);
        jsonObject.put("msg","获取成功");
        jsonObject.put("projects",projects.getRecords());
        jsonObject.put("pages",projects.getPages());
        return jsonObject.toString();
    }
}
