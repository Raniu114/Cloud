package org.raniu.project.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



/**
 * @projectName: cloud
 * @package: org.raniu.vo
 * @className: ProjectVo
 * @author: Raniu
 * @description: TODO
 * @date: 2023/12/12 17:34
 * @version: 1.0
 */
@Data
@ApiModel(value = "项目请求参数", description = "项目请求参数")
public class ProjectVo {
    @ApiModelProperty(value = "项目id",name = "id",required = true)
    @NotNull(message = "项目id不能为空")
    private String id;

    @ApiModelProperty(value = "项目名称",name = "name", required = true)
    @NotNull(message = "项目名称不能为空")
    private String name;

    @ApiModelProperty(value = "所有者", name = "owner",required = false)
    private Long owner;
}