package org.raniu.project.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title = "项目请求参数", description = "项目请求参数")
public class ProjectVo {
    @Schema(description = "项目id",name = "id",required = true)
    @NotNull(message = "项目id不能为空")
    private String id;

    @Schema(description = "项目名称",name = "name", required = true)
    @NotNull(message = "项目名称不能为空")
    private String name;

    @Schema(description = "所有者", name = "owner",required = false)
    private Long owner;

    @Schema(description = "项目地址", name = "addr", required = false)
    private String addr;

    @Schema(description = "项目类型", name = "type", required = true)
    private Long type;

    @Schema(description = "项目类型名称", name = "typeName", required = false)
    private String typeName;

    @Schema(description = "项目创建人", name = "creatUser", required = false)
    private Long creatUser;

    @Schema(description = "项目创建时间", name = "creatTime", required = false)
    private Long creatTime;

}