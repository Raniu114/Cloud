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
@ApiModel(description = "用户实体类")
public class UserDTO {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户密码")
    private String password;

    @ApiModelProperty("权限")
    private Integer permissions;

}
