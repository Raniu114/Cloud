package org.raniu.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.raniu.api.enums.ResultCode;
import org.raniu.api.vo.Result;
import org.raniu.common.enums.PermissionsEnum;
import org.raniu.common.utils.UserContext;
import org.raniu.project.domain.po.ProjectPo;
import org.raniu.project.domain.po.ProjectTypePo;
import org.raniu.project.domain.vo.ProjectTypeVo;
import org.raniu.project.mapper.ProjectMapper;
import org.raniu.project.mapper.ProjectTypeMapper;
import org.raniu.project.service.ProjectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @projectName: IOTCloud
 * @package: org.raniu.project.service.impl
 * @className: ProjectTypeServiceImpl
 * @author: Raniu
 * @description: TODO
 * @date: 2024/6/17 17:04
 * @version: 1.0
 */

@Service
public class ProjectTypeServiceImpl extends ServiceImpl<ProjectTypeMapper, ProjectTypePo> implements ProjectTypeService {

    @Autowired
    private ProjectTypeMapper projectTypeMapper;

    public Page<ProjectTypePo> list(Integer page, Integer size) {
        Page<ProjectTypePo> projectTypePage = new Page<>(page, size);
        QueryWrapper<ProjectTypePo> queryWrapper = new QueryWrapper<>();
        return this.projectTypeMapper.selectPage(projectTypePage, queryWrapper);
    }

    public Page<ProjectTypePo> select(String key, Long user, Integer page, Integer size) {
        Page<ProjectTypePo> projectTypePage = new Page<>(page, size);
        QueryWrapper<ProjectTypePo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", key);
        return this.projectTypeMapper.selectPage(projectTypePage, queryWrapper);
    }

    @Override
    public Result<ProjectTypePo> addProjectType(ProjectTypeVo projectTypeVo, HttpServletResponse response) {
        ProjectTypePo projectTypePo = new ProjectTypePo();
        projectTypePo.setTypeName(projectTypePo.getTypeName());
        if (!save(projectTypePo)) {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS,"添加失败，请检查名称是否重复");
        }
        return Result.success("添加成功");
    }

    @Override
    public Result<ProjectTypePo> updateProjectType(ProjectTypeVo projectTypeVo, HttpServletResponse response) {
        ProjectTypePo projectTypePo = new ProjectTypePo();
        projectTypePo.setTypeName(projectTypePo.getTypeName());
        if (updateById(projectTypePo)) {
            return Result.success("更新成功");
        } else {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS,"更新失败");
        }
    }

    @Override
    public Result<ProjectTypePo> deleteProjectType(String id, HttpServletResponse response) {
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
    public Result<List<ProjectTypePo>> listProjectType(Integer page, Integer size, HttpServletResponse response) {
        if (page == null || size == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING,"参数不可为空");
        }
        Page<ProjectTypePo> projectTypes;
        projectTypes = list(page, size);
        if (!projectTypes.getRecords().isEmpty()) {
            return Result.success(projectTypes.getRecords(),projectTypes.getPages());
        } else {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS,"获取失败");
        }
    }

    @Override
    public Result<ProjectTypePo> getProjectType(String id, HttpServletResponse response) {
        if (id == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING,"参数不可为空");
        }
        ProjectTypePo projectTypePo = getById(id);
        if (projectTypePo == null) {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS,"未查询到项目");
        }
        return Result.success(projectTypePo);
    }

    @Override
    public Result<List<ProjectTypePo>> selectProjectType(String key, Integer page, Integer size, HttpServletResponse response) {
        if (key == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING,"参数不可为空");
        }
        Page<ProjectTypePo> projectTypePo;
        projectTypePo = select(key,UserContext.getUser(), page, size);
        return Result.success(projectTypePo.getRecords(),projectTypePo.getPages());
    }
}
