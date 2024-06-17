package org.raniu.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.raniu.api.vo.Result;
import org.raniu.project.domain.po.ProjectTypePo;
import org.raniu.project.domain.vo.ProjectTypeVo;

import java.util.List;

public interface ProjectTypeService extends IService<ProjectTypePo> {
    Result<ProjectTypePo> addProjectType(ProjectTypeVo projectTypeVo, HttpServletResponse response);

    Result<ProjectTypePo> updateProjectType(ProjectTypeVo projectTypeVo, HttpServletResponse response);

    Result<ProjectTypePo> deleteProjectType(String id, HttpServletResponse response);

    Result<List<ProjectTypePo>> listProjectType(Integer page, Integer size, HttpServletResponse response);

    Result<ProjectTypePo> getProjectType(String id, HttpServletResponse response);

    Result<List<ProjectTypePo>> selectProjectType(String key, Integer page, Integer size, HttpServletResponse response);
}
