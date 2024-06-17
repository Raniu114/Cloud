package org.raniu.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
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

    IPage<ProjectVo> list(Integer page, Integer size, Long userId, Integer permission);

    IPage<ProjectVo> select(String key, Long user, Integer page, Integer size, Integer permission);

    Result<List<ProjectVo>> selectProject(String key, Integer page, Integer size, HttpServletResponse response);

    Result<ProjectVo> getProject(String id, HttpServletResponse response);

    Result<List<ProjectVo>> listProject(Integer page, Integer size, HttpServletResponse response);

    Result<ProjectPo> deleteProject(String id, HttpServletResponse response);

    Result<ProjectPo> updateProject(ProjectVo projectVo, HttpServletResponse response);

    Result<ProjectPo> addProject(ProjectVo projectVo, HttpServletResponse response);

}
