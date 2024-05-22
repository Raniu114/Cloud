package org.raniu.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.raniu.project.domain.po.ProjectPo;


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
    Page<ProjectPo> list(Integer page, Integer size);

    Page<ProjectPo> list(Integer page, Integer size, Long userId);

    Page<ProjectPo> select(String key, Integer page, Integer size);

    Page<ProjectPo> select(String key, Long user, Integer page, Integer size);

    List<ProjectPo> select(Long user);
}
