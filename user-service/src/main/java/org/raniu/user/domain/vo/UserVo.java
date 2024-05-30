package org.raniu.user.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.user.domain.vo
 * @className: UserVo
 * @author: Raniu
 * @description: TODO
 * @date: 2024/5/30 16:32
 * @version: 1.0
 */

@Data
@Schema(title = "用户参数", description = "用户参数")
public class UserVo {

    @Schema(description = "用户名", name = "username")
    private String username;

    @Schema(description = "密码（加密后）", name = "password")
    private String password;

    @Schema(description = "权限", name = "permissions")
    private Integer permissions;

    @Schema(description = "权限表", name = "auth")
    private String auth;

    @Schema(description = "邮箱", name = "email")
    private String email;

    @Schema(description = "联系电话", name = "phone")
    private String phone;

    @Schema(description = "地址", name = "addr")
    private String addr;

}
