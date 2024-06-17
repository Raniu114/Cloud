package org.raniu.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;
import org.raniu.api.enums.ResultCode;
import org.raniu.api.vo.Result;
import org.raniu.common.enums.PermissionsEnum;
import org.raniu.common.utils.UserContext;
import org.raniu.project.domain.po.ProjectPo;
import org.raniu.project.domain.po.ProjectTypePo;
import org.raniu.project.domain.vo.ProjectVo;
import org.raniu.project.mapper.ProjectMapper;
import org.raniu.project.service.ProjectService;
import org.raniu.project.service.ProjectTypeService;
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

    @Autowired
    private ProjectTypeService projectTypeService;

    @Override
    public IPage<ProjectVo> list(Integer page, Integer size, Long userId, Integer permission) {
        Page<ProjectVo> projectPage = new Page<>(page, size);
        QueryWrapper<ProjectVo> queryWrapper = new QueryWrapper<>();
        if (permission == PermissionsEnum.USER.ordinal()) {
            queryWrapper.eq("owner", userId);
        } else if (permission == PermissionsEnum.ADMIN.ordinal()) {
            queryWrapper.eq("create_user", userId);
        } else if (permission == PermissionsEnum.SUPER_ADMIN.ordinal()) {
            return this.projectMapper.findWithType(projectPage, queryWrapper);
        } else {
            return null;
        }
        return this.projectMapper.findWithType(projectPage, queryWrapper);
    }

    @Override
    public IPage<ProjectVo> select(String key, Long user, Integer page, Integer size, Integer permission) {
        Page<ProjectVo> projectPage = new Page<>(page, size);
        QueryWrapper<ProjectVo> queryWrapper = new QueryWrapper<>();
        if (permission == PermissionsEnum.USER.ordinal()) {
            queryWrapper.like("name", key).eq("owner", user);
        } else if (permission == PermissionsEnum.ADMIN.ordinal()) {
            queryWrapper.like("name", key).eq("create_user", user);
        } else if (permission == PermissionsEnum.SUPER_ADMIN.ordinal()) {
            queryWrapper.like("name", key);
        } else {
            return null;
        }
        return this.projectMapper.findWithType(projectPage, queryWrapper);
    }

    @Override
    public Result<List<ProjectVo>> selectProject(String key, Integer page, Integer size, HttpServletResponse response) {
        if (key == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING,"参数不可为空");
        }
        IPage<ProjectVo> projects;
        projects = select(key,UserContext.getUser(), page, size, UserContext.getPermissions());
        return Result.success(projects.getRecords(),projects.getPages());
    }

    @Override
    public Result<ProjectVo> getProject(String id, HttpServletResponse response) {
        if (id == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING,"参数不可为空");
        }
        ProjectPo projectPo = getById(id);
        if (projectPo == null) {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS,"未查询到项目");
        }
        ProjectTypePo type = projectTypeService.getById(projectPo.getType());
        ProjectVo projectVo = new ProjectVo();
        projectVo.setCreatTime(projectPo.getCreatTime());
        projectVo.setName(projectPo.getName());
        projectVo.setType(type.getId());
        projectVo.setAddr(projectPo.getAddr());
        projectVo.setId(projectPo.getId());
        projectVo.setOwner(projectPo.getOwner());
        projectVo.setTypeName(type.getTypeName());
        return Result.success(projectVo);
    }

    @Override
    public Result<List<ProjectVo>> listProject(Integer page, Integer size, HttpServletResponse response) {
        if (page == null || size == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING,"参数不可为空");
        }
        IPage<ProjectVo> projects;
        projects = list(page, size, UserContext.getUser(), UserContext.getPermissions());
        if (!projects.getRecords().isEmpty()) {
            return Result.success(projects.getRecords(),projects.getPages());
        } else {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS,"获取失败");
        }
    }

    @Override
    public Result<ProjectPo> deleteProject(String id, HttpServletResponse response) {
        if (id == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING,"参数不可为空");
        }
        if (removeById(id)) {
            return Result.success("删除成功");
        } else {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS,"未查询到项目");
        }
    }

    @Override
    public Result<ProjectPo> updateProject(ProjectVo projectVo, HttpServletResponse response) {
        ProjectPo projectPo = new ProjectPo();
        projectPo.setOwner(projectVo.getOwner());
        projectPo.setName(projectVo.getName());
        projectPo.setId(projectVo.getId());
        projectPo.setAddr(projectVo.getAddr());
        if (updateById(projectPo)) {
            return Result.success("更新成功");
        } else {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS,"更新失败");
        }
    }

    @Override
    public Result<ProjectPo> addProject(ProjectVo projectVo, HttpServletResponse response) {
        ProjectPo projectPo = new ProjectPo();
        projectPo.setId(projectVo.getId());
        projectPo.setName(projectVo.getName());
        projectPo.setOwner(projectVo.getOwner());
        projectPo.setAddr(projectVo.getAddr());
        projectPo.setCreatUser(UserContext.getUser());
        projectPo.setCreatTime(System.currentTimeMillis());
        if (!save(projectPo)) {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS,"添加失败，请检查id是否重复");
        }
        return Result.success("添加成功");
    }
}
