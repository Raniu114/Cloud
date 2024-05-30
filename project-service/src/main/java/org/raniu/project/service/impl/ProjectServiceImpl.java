package org.raniu.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.raniu.common.enums.PermissionsEnum;
import org.raniu.common.utils.UserContext;
import org.raniu.project.domain.po.ProjectPo;
import org.raniu.project.domain.vo.ProjectVo;
import org.raniu.project.mapper.ProjectMapper;
import org.raniu.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author raniu
 * @since 2023-12-11
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, ProjectPo> implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public Page<ProjectPo> list(Integer page, Integer size, Long userId, Integer permission) {
        Page<ProjectPo> projectPage = new Page<>(page, size);
        QueryWrapper<ProjectPo> queryWrapper = new QueryWrapper<>();
        if (permission == PermissionsEnum.USER.ordinal()) {
            queryWrapper.eq("owner", userId);
        } else if (permission == PermissionsEnum.ADMIN.ordinal()) {
            queryWrapper.eq("create_user", userId);
        } else if (permission == PermissionsEnum.SUPER_ADMIN.ordinal()) {
            return this.projectMapper.selectPage(projectPage, queryWrapper);
        } else {
            return null;
        }
        return this.projectMapper.selectPage(projectPage, queryWrapper);
    }

    @Override
    public Page<ProjectPo> select(String key, Long user, Integer page, Integer size, Integer permission) {
        Page<ProjectPo> projectPage = new Page<>(page, size);
        QueryWrapper<ProjectPo> queryWrapper = new QueryWrapper<>();
        if (permission == PermissionsEnum.USER.ordinal()) {
            queryWrapper.like("name", key).eq("owner", user);
        } else if (permission == PermissionsEnum.ADMIN.ordinal()) {
            queryWrapper.like("name", key).eq("create_user", user);
        } else if (permission == PermissionsEnum.SUPER_ADMIN.ordinal()) {
            queryWrapper.like("name", key);
        } else {
            return null;
        }
        return this.projectMapper.selectPage(projectPage, queryWrapper);
    }

    @Override
    public String selectProject(String key, Integer page, Integer size, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        if (key == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "参数不可为空");
            return jsonObject.toString();
        }
        Page<ProjectPo> projects;
        projects = select(key,UserContext.getUser(), page, size, UserContext.getPermissions());
        jsonObject.put("status",1);
        jsonObject.put("msg","获取成功");
        jsonObject.put("projects",projects.getRecords());
        jsonObject.put("pages",projects.getPages());
        return jsonObject.toString();
    }

    @Override
    public String getProject(String id, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        if (id == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "参数不可为空");
            return jsonObject.toString();
        }
        ProjectPo projectPo = getById(id);
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

    @Override
    public String listProject(Integer page, Integer size, HttpServletResponse response) {

        JSONObject jsonObject = new JSONObject();
        if (page == null || size == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "参数不可为空");
            return jsonObject.toString();
        }
        Page<ProjectPo> projects;
        projects = list(page, size, UserContext.getUser(), UserContext.getPermissions());

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

    @Override
    public String deleteProject(String id, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        if (id == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "参数不可为空");
            return jsonObject.toString();
        }
        if (removeById(id)) {
            jsonObject.put("status", 1);
            jsonObject.put("msg", "删除成功");
        } else {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "未找到项目");
        }
        return jsonObject.toString();
    }

    @Override
    public String updateProject(ProjectVo projectVo, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        ProjectPo projectPo = new ProjectPo();
        projectPo.setOwner(projectVo.getOwner());
        projectPo.setName(projectVo.getName());
        projectPo.setId(projectVo.getId());
        projectPo.setAddr(projectVo.getAddr());
        if (updateById(projectPo)) {
            jsonObject.put("status", 1);
            jsonObject.put("msg", "更新成功");
        } else {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "更新失败");
        }
        return jsonObject.toString();
    }

    @Override
    public String addProject(ProjectVo projectVo, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        ProjectPo projectPo = new ProjectPo();
        projectPo.setId(projectVo.getId());
        projectPo.setName(projectVo.getName());
        projectPo.setOwner(projectVo.getOwner());
        projectPo.setAddr(projectVo.getAddr());
        projectPo.setOwner(UserContext.getUser());
        projectPo.setCreatTime(System.currentTimeMillis());
        if (!save(projectPo)) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "添加失败，请检查id是否重复");
            return jsonObject.toString();
        }
        jsonObject.put("status", 1);
        jsonObject.put("msg", "添加成功");
        return jsonObject.toString();
    }
}
