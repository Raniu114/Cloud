package org.raniu.project.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.raniu.api.dto.ProjectDTO;
import org.raniu.project.domain.po.ProjectPo;
import org.raniu.project.domain.vo.ProjectVo;


/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author raniu
 * @since 2023-12-11
 */
public interface ProjectMapper extends BaseMapper<ProjectPo> {

    IPage<ProjectDTO> findWithType(Page<ProjectDTO> page, @Param(Constants.WRAPPER) QueryWrapper<ProjectDTO> queryWrapper);

}
