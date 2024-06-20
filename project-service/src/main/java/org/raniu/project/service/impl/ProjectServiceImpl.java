package org.raniu.project.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;
import org.raniu.api.dto.ProjectDTO;
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
import java.util.Map;


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

    public IPage<ProjectDTO> list(Integer page, Integer size) {
        Page<ProjectDTO> projectPage = new Page<>(page, size);
        QueryWrapper<ProjectDTO> queryWrapper = new QueryWrapper<>();
        if (UserContext.getPermissions() == PermissionsEnum.USER.ordinal()) {
            queryWrapper.eq("owner",  UserContext.getUser());
        } else if (UserContext.getPermissions() == PermissionsEnum.ADMIN.ordinal()) {
            queryWrapper.eq("create_user",  UserContext.getUser());
        } else if (UserContext.getPermissions() == PermissionsEnum.SUPER_ADMIN.ordinal()) {
            return this.projectMapper.findWithType(projectPage, queryWrapper);
        } else {
            return null;
        }
        return this.projectMapper.findWithType(projectPage, queryWrapper);
    }

    public IPage<ProjectDTO> select(String key, Integer page, Integer size) {
        Page<ProjectDTO> projectPage = new Page<>(page, size);
        QueryWrapper<ProjectDTO> queryWrapper = new QueryWrapper<>();
        if (UserContext.getPermissions() == PermissionsEnum.USER.ordinal()) {
            queryWrapper.like("name", key).eq("owner",  UserContext.getUser());
        } else if (UserContext.getPermissions() == PermissionsEnum.ADMIN.ordinal()) {
            queryWrapper.like("name", key).eq("create_user",  UserContext.getUser());
        } else if (UserContext.getPermissions() == PermissionsEnum.SUPER_ADMIN.ordinal()) {
            queryWrapper.like("name", key);
        } else {
            return null;
        }
        return this.projectMapper.findWithType(projectPage, queryWrapper);
    }

    public IPage<ProjectDTO> preciseSelect(List<String> keys,List<String> values, Integer page, Integer size) {
        Page<ProjectDTO> projectPage = new Page<>(page, size);
        QueryWrapper<ProjectDTO> queryWrapper = new QueryWrapper<>();
        Map<String, String> params = ArrayUtil.zip(ArrayUtil.toArray(keys, String.class), ArrayUtil.toArray(values, String.class));
        if (UserContext.getPermissions() == PermissionsEnum.USER.ordinal()) {
            queryWrapper.allEq(params).eq("owner", UserContext.getUser());
        } else if (UserContext.getPermissions() == PermissionsEnum.ADMIN.ordinal()) {
            queryWrapper.allEq(params).eq("create_user", UserContext.getUser());
        } else if (UserContext.getPermissions() == PermissionsEnum.SUPER_ADMIN.ordinal()) {
            queryWrapper.allEq(params);
        } else {
            return null;
        }
        return this.projectMapper.findWithType(projectPage, queryWrapper);
    }

    @Override
    public Result<List<ProjectDTO>> selectProject(String key, Integer page, Integer size, HttpServletResponse response) {
        if (key == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "参数不可为空");
        }
        IPage<ProjectDTO> projects = select(key, page, size);
        if (!projects.getRecords().isEmpty()) {
            return Result.success(projects.getRecords(), projects.getPages());
        } else {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "获取失败");
        }
    }

    @Override
    public Result<ProjectDTO> getProject(String id, HttpServletResponse response) {
        if (id == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "参数不可为空");
        }
        ProjectPo projectPo = getById(id);
        if (projectPo == null) {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "未查询到项目");
        }
        ProjectTypePo type = projectTypeService.getById(projectPo.getType());
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setCreatTime(projectPo.getCreatTime());
        projectDTO.setName(projectPo.getName());
        projectDTO.setType(type.getId());
        projectDTO.setAddr(projectPo.getAddr());
        projectDTO.setId(projectPo.getId());
        projectDTO.setOwner(projectPo.getOwner());
        projectDTO.setTypeName(type.getTypeName());
        return Result.success(projectDTO);
    }

    @Override
    public Result<List<ProjectDTO>> listProject(Integer page, Integer size, HttpServletResponse response) {
        if (page == null || size == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "参数不可为空");
        }
        IPage<ProjectDTO> projects;
        projects = list(page, size);
        if (!projects.getRecords().isEmpty()) {
            return Result.success(projects.getRecords(), projects.getPages());
        } else {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "获取失败");
        }
    }

    @Override
    public Result<ProjectPo> deleteProject(String id, HttpServletResponse response) {
        if (id == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "参数不可为空");
        }
        if (removeById(id)) {
            return Result.success("删除成功");
        } else {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "未查询到项目");
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
            return Result.error(ResultCode.ERROR_PARAMETERS, "更新失败");
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
            return Result.error(ResultCode.ERROR_PARAMETERS, "添加失败，请检查id是否重复");
        }
        return Result.success("添加成功");
    }

    @Override
    public Result<List<ProjectDTO>> preciseSelectProject(List<String> keys, List<String> values, Integer page, Integer size, HttpServletResponse response) {
        if (keys == null || values == null || keys.size() != values.size()) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "参数不可为空");
        }
        if (UserContext.getPermissions() == 1){
            if (keys.contains("create_user")){
                response.setStatus(403);
                return Result.error(ResultCode.ERROR_PARAMETERS,"不符合权限的参数");
            }
        }else if (UserContext.getPermissions() == 0){
            if (keys.contains("owner") || keys.contains("create_user")){
                response.setStatus(403);
                return Result.error(ResultCode.ERROR_PARAMETERS,"不符合权限的参数");
            }
        }
        IPage<ProjectDTO> projects = this.preciseSelect(keys, values, page, size);
        if (!projects.getRecords().isEmpty()) {
            return Result.success(projects.getRecords(), projects.getPages());
        } else {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "获取失败");
        }
    }
}
