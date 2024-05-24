package org.raniu.project.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.raniu.common.utils.UserContext;
import org.raniu.project.domain.po.ProjectPo;
import org.raniu.project.mapper.ProjectMapper;
import org.raniu.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Page<ProjectPo> list(Integer page, Integer size) {
        Page<ProjectPo> projectPage = new Page<>(page, size);
        return this.projectMapper.selectPage(projectPage, null);
    }

    @Override
    public Page<ProjectPo> list(Integer page, Integer size, Long userId) {
        Page<ProjectPo> projectPage = new Page<>(page, size);
        QueryWrapper<ProjectPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner", userId);
        return this.projectMapper.selectPage(projectPage, queryWrapper);
    }

    @Override
    public Page<ProjectPo> select(String key, Integer page, Integer size) {
        Page<ProjectPo> projectPage = new Page<>(page, size);
        QueryWrapper<ProjectPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", key).or().eq("owner", key);
        return this.projectMapper.selectPage(projectPage, queryWrapper);
    }

    @Override
    public Page<ProjectPo> select(String key, Long user, Integer page, Integer size, Integer permission) {
        Page<ProjectPo> projectPage = new Page<>(page, size);
        QueryWrapper<ProjectPo> queryWrapper = new QueryWrapper<>();
        if (permission == 0) {
            queryWrapper.like("name", key).eq("owner", user);
        } else if (permission == 1) {
            queryWrapper.like("name", key).eq("create_user", user);
        }
        return this.projectMapper.selectPage(projectPage, queryWrapper);
    }

    @Override
    public String selectProject(String key, String page, String size, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        if (key == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "参数不可为空");
            return jsonObject.toString();
        }
        cn.hutool.json.JSONObject authJson = JSONUtil.parseObj(UserContext.getAuth());
        if("none".equals(authJson.get("project")) && UserContext.getPermissions() < 2){
            response.setStatus(403);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "权限不足");
            return jsonObject.toString();
        }
        Page<ProjectPo> projects;
        if (UserContext.getPermissions() > 1) {
            projects = select(key, Integer.parseInt(page), Integer.parseInt(size));
        }else {
            projects = select(key,UserContext.getUser(),Integer.parseInt(page), Integer.parseInt(size), UserContext.getPermissions());
        }
        jsonObject.put("status",1);
        jsonObject.put("msg","获取成功");
        jsonObject.put("projects",projects.getRecords());
        jsonObject.put("pages",projects.getPages());
        return jsonObject.toString();
    }
}
