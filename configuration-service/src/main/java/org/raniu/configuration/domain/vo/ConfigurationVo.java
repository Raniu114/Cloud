package org.raniu.configuration.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import jakarta.validation.constraints.NotNull;


/**
 * @projectName: cloud
 * @package: org.raniu.vo
 * @className: ConfigurationVo
 * @author: Raniu
 * @description: TODO
 * @date: 2024/1/10 14:46
 * @version: 1.0
 */
@Data
@ApiModel(value = "组态参数", description = "组态参数")
public class ConfigurationVo {
    @NotNull(message = "名称不能为空")
    @ApiModelProperty(value = "组态名称", name = "name", required = true)
    private String name;
    @NotNull(message = "项目id不能为空")
    @ApiModelProperty(value = "项目id", name = "projectId", required = true)
    private String projectId;
    @NotNull(message = "页面内容不能为空")
    @ApiModelProperty(value = "页面内容", name = "page", required = true)
    private String page;

}
