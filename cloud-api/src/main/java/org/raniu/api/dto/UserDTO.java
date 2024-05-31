package org.raniu.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.hutool.json.JSONObject;
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

    @ApiModelProperty("权限表")
    private JSONObject auth;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("联系电话")
    private String phone;

    @ApiModelProperty("创建用户")
    private Long creatUser;

    @ApiModelProperty("创建时间")
    private Long creatTime;

    @ApiModelProperty("地址")
    private String addr;

}
