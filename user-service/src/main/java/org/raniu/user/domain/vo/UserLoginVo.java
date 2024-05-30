package org.raniu.user.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @projectName: cloud
 * @package: org.raniu.vo
 * @className: LoginVo
 * @author: Raniu
 * @description: TODO
 * @date: 2023/12/11 16:33
 * @version: 1.0
 */
@Data
@ApiModel(value = "用户登录参数",description = "用户登录参数")
public class UserLoginVo {
    @NotNull(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名",name = "username",required = true)
    private String username;
    @ApiModelProperty(value = "密码（加密后）",name = "password",required = true)
    @NotNull(message = "密码不能为空")
    private String password;
}
