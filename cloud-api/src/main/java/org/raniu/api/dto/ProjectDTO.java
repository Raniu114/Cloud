package org.raniu.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <p>
 *
 * </p>
 *
 * @author raniu
 * @since 2023-12-11
 */
@Data
@ApiModel(description = "项目实体类")
public class ProjectDTO {

    @ApiModelProperty("项目id")
    private String id;

    @ApiModelProperty("项目名称")
    private String name;

    @ApiModelProperty("项目所有人")
    private Long owner;

    @ApiModelProperty("项目创建人")
    private String creatUser;

    @ApiModelProperty("项目地址")
    private String addr;

    @ApiModelProperty("项目创建时间")
    private Long creatTime;

    @ApiModelProperty("项目类型")
    private Long type;
}
