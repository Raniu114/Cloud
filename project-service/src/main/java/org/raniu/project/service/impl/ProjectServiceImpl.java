package org.raniu.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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
    public Page<ProjectPo> select(String key, Long user, Integer page, Integer size) {
        Page<ProjectPo> projectPage = new Page<>(page, size);
        QueryWrapper<ProjectPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", key).eq("owner", user);
        return this.projectMapper.selectPage(projectPage, queryWrapper);
    }

    @Override
    public List<ProjectPo> select(Long user) {
        QueryWrapper<ProjectPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner", user);
        return this.projectMapper.selectList(queryWrapper);
    }
}
