package org.raniu.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.raniu.api.dto.ProjectDTO;
import org.raniu.api.vo.Result;
import org.raniu.project.domain.po.ProjectPo;
import org.raniu.project.domain.vo.ProjectVo;

import java.util.List;


/**
 * <p>
 * 服务类
 * </p>
 *
 * @author raniu
 * @since 2023-12-11
 */
public interface ProjectService extends IService<ProjectPo> {


    Result<List<ProjectDTO>> selectProject(String key, Integer page, Integer size, HttpServletResponse response);

    Result<ProjectDTO> getProject(String id, HttpServletResponse response);

    Result<List<ProjectDTO>> listProject(Integer page, Integer size, HttpServletResponse response);

    Result<ProjectPo> deleteProject(String id, HttpServletResponse response);

    Result<ProjectPo> updateProject(ProjectVo projectVo, HttpServletResponse response);

    Result<ProjectPo> addProject(ProjectVo projectVo, HttpServletResponse response);

    Result<List<ProjectDTO>> preciseSelectProject(List<String> keys, List<String> values, Integer page, Integer size, HttpServletResponse response);
}
