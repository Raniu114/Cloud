package org.raniu.user.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.raniu.api.dto.UserDTO;
import org.raniu.api.vo.Result;
import org.raniu.user.domain.po.UserPo;
import org.raniu.user.domain.vo.UserLoginVo;
import org.raniu.user.domain.vo.UserVo;
import org.raniu.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author raniu
 * @since 2023-12-11
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
@Tag(name = "用户接口", description = "用户接口")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login/admin")
    @Operation(summary = "管理员登录", description = "管理员登录")
    @ApiResponses({
            @ApiResponse(responseCode = "412", description = "找不到用户"),
            @ApiResponse(responseCode = "400", description = "密钥错误")
    })
    public Result<UserDTO> loginAdmin(@RequestBody @Valid UserLoginVo userLoginVo, HttpServletResponse response, HttpServletRequest request) {
        return this.userService.loginAdmin(userLoginVo, response);
    }

    @PostMapping("/login/user")
    @Operation(summary = "用户登录", description = "用户登录")
    @ApiResponses({
            @ApiResponse(responseCode = "412", description = "找不到用户"),
            @ApiResponse(responseCode = "400", description = "密钥错误")
    })
    public Result<UserDTO> loginUser(@RequestBody @Valid UserLoginVo userLoginVo, HttpServletResponse response, HttpServletRequest request) {
        return this.userService.loginUser(userLoginVo, response);
    }


    @GetMapping("/token")
    @Operation(summary = "token刷新", description = "token刷新")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "未携带token"),
            @ApiResponse(responseCode = "412", description = "找不到用户")
    })
    public Result<UserDTO> getToken(@CookieValue(name = "RefreshToken", required = false) String refreshToken, HttpServletResponse response, HttpServletRequest request) {
        return this.userService.getToken(refreshToken, response);
    }

    @PostMapping("/add")
    @Operation(summary = "添加用户", description = "添加用户")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "未携带token"),
            @ApiResponse(responseCode = "403", description = "权限不足"),
            @ApiResponse(responseCode = "400", description = "密钥错误")
    })
    public Result<UserVo> addUser(@RequestBody @Valid UserVo userVo, HttpServletResponse response, HttpServletRequest request) {
        return this.userService.addUser(userVo, response);
    }

    @GetMapping("/list")
    @Operation(summary = "用户列表", description = "用户列表")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "未携带token"),
            @ApiResponse(responseCode = "403", description = "权限不足"),
            @ApiResponse(responseCode = "412", description = "参数缺失或没有用户")
    })
    @Parameters({
            @Parameter(name = "page", description = "分页页码", required = true),
            @Parameter(name = "size", description = "单页长度", required = true)
    })
    public Result<List<UserDTO>> userList(@RequestParam("page") Integer page, @RequestParam("size") Integer size, HttpServletRequest request, HttpServletResponse response) {
        return this.userService.userList(page, size, response);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除用户", description = "删除用户")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "未携带token"),
            @ApiResponse(responseCode = "403", description = "权限不足"),
            @ApiResponse(responseCode = "412", description = "参数缺失或找不到用户")
    })
    @Parameter(name = "id", description = "用户id")
    public Result<UserVo> deleteUser(@RequestParam("id") Long id, HttpServletResponse response, HttpServletRequest request) {
        return this.userService.delUser(id, response);
    }

    @PostMapping("/update")
    @Operation(summary = "更新用户信息", description = "更新用户信息")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "未携带token"),
            @ApiResponse(responseCode = "400", description = "密钥错误"),
            @ApiResponse(responseCode = "403", description = "权限不足"),
            @ApiResponse(responseCode = "412", description = "找不到用户")
    })
    public Result<UserVo> updateUser(@RequestBody @Valid UserVo userVo, HttpServletRequest request, HttpServletResponse response) {
        return this.userService.updateUser(userVo, response);
    }

    @GetMapping("/get")
    @Operation(summary = "获取用户", description = "获取单个用户")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "未携带token"),
            @ApiResponse(responseCode = "403", description = "权限与操作不符"),
            @ApiResponse(responseCode = "412", description = "参数缺失或找不到用户")
    })
    @Parameter(name = "id", description = "用户id")
    public Result<UserDTO> getUser(@RequestParam("id") Long id, HttpServletResponse response, HttpServletRequest request) {
        return this.userService.getUser(id, response);
    }

    @GetMapping("/select")
    @Operation(summary = "查询用户", description = "查询用户")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "未携带token"),
            @ApiResponse(responseCode = "403", description = "权限不足"),
            @ApiResponse(responseCode = "412", description = "参数缺失或找不到用户")
    })
    @Parameters({
            @Parameter(name = "key", description = "关键词"),
            @Parameter(name = "page", description = "页码"),
            @Parameter(name = "size", description = "最大条目数")
    })
    public Result<List<UserDTO>> selectUser(@RequestParam(name = "key") String key, @RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size, HttpServletResponse response, HttpServletRequest request) {
        return this.userService.selectUser(key, page, size, response);
    }

}

