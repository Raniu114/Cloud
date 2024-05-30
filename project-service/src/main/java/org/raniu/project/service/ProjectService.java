package org.raniu.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.raniu.project.domain.po.ProjectPo;
import org.raniu.project.domain.vo.ProjectVo;


/**
 * <p>
 * 服务类
 * </p>
 *
 * @author raniu
 * @since 2023-12-11
 */
public interface ProjectService extends IService<ProjectPo> {

    Page<ProjectPo> list(Integer page, Integer size, Long userId, Integer permission);

    Page<ProjectPo> select(String key, Long user, Integer page, Integer size, Integer permission);

    String selectProject(String key, Integer page, Integer size, HttpServletResponse response);

    String getProject(String id, HttpServletResponse response);

    String listProject(Integer page, Integer size, HttpServletResponse response);

    String deleteProject(String id, HttpServletResponse response);

    String updateProject(ProjectVo projectVo, HttpServletResponse response);

    String addProject(ProjectVo projectVo, HttpServletResponse response);

}
