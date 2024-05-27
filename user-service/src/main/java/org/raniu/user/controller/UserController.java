package org.raniu.user.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.raniu.api.dto.UserDTO;
import org.raniu.user.domain.po.UserPo;
import org.raniu.user.domain.vo.UserLoginVo;
import org.raniu.user.service.RSAService;
import org.raniu.user.service.TokenService;
import org.raniu.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

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
@Api(tags = "用户接口", value = "用户接口")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RSAService rsaService;


    @PostMapping("/login/admin")
    @ApiOperation(value = "登录", notes = "用户登录")
    @ApiResponses({
            @ApiResponse(code = 412, message = "找不到用户"),
            @ApiResponse(code = 400, message = "密钥错误")
    })
    public String loginAdmin(@RequestBody @Valid UserLoginVo userLoginVo, BindingResult bindingResult, HttpServletResponse response, HttpServletRequest request) {
        for (ObjectError error : bindingResult.getAllErrors()) {
            return error.getDefaultMessage();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            userLoginVo.setPassword(this.rsaService.decrypt(userLoginVo.getPassword()));
        } catch (Exception e) {
            response.setStatus(400);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "请验证密钥是否正确");
            return jsonObject.toString();
        }
        UserPo user = this.userService.login(userLoginVo);
        if (user == null || user.getPermissions() != 1) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "用户不存在");
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setPermissions(user.getPermissions());
        userDTO.setUsername(user.getUsername());
        userDTO.setAuth(user.getAuth());
        jsonObject.put("status", 1);
        jsonObject.put("msg", "登陆成功");
        jsonObject.put("user", BeanMap.create(userDTO));
        response.setStatus(200);
        Cookie cookie = new Cookie("AccessToken", this.tokenService.getAccessToken(userDTO));
        cookie.setMaxAge(30 * 60);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setDomain("zhxy.fjhnkj.com");
        Cookie cookie1 = new Cookie("RefreshToken", this.tokenService.getRefreshToken(userDTO));
        cookie1.setSecure(true);
        cookie1.setHttpOnly(true);
        cookie1.setMaxAge(30 * 24 * 60 * 60);
        cookie1.setPath("/");
        cookie1.setDomain("zhxy.fjhnkj.com");
        response.addCookie(cookie);
        response.addCookie(cookie1);
        return jsonObject.toString();
    }

    @PostMapping("/login/user")
    @ApiOperation(value = "登录", notes = "用户登录")
    @ApiResponses({
            @ApiResponse(code = 412, message = "找不到用户"),
            @ApiResponse(code = 400, message = "密钥错误")
    })
    public String loginUser(@RequestBody @Valid UserLoginVo userLoginVo, BindingResult bindingResult, HttpServletResponse response, HttpServletRequest request) {
        for (ObjectError error : bindingResult.getAllErrors()) {
            return error.getDefaultMessage();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            userLoginVo.setPassword(this.rsaService.decrypt(userLoginVo.getPassword()));
        } catch (Exception e) {
            response.setStatus(400);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "请验证密钥是否正确");
            return jsonObject.toString();
        }
        UserPo user = this.userService.login(userLoginVo);
        if (user == null || user.getPermissions() != 1) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "用户不存在");
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setPermissions(user.getPermissions());
        userDTO.setUsername(user.getUsername());
        userDTO.setAuth(user.getAuth());
        jsonObject.put("status", 1);
        jsonObject.put("msg", "登陆成功");
        jsonObject.put("user", BeanMap.create(user));
        Cookie cookie = new Cookie("AccessToken", this.tokenService.getAccessToken(userDTO));
        cookie.setMaxAge(30 * 60);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setDomain("zhxy.fjhnkj.com");
        Cookie cookie1 = new Cookie("RefreshToken", this.tokenService.getRefreshToken(userDTO));
        cookie1.setSecure(true);
        cookie1.setMaxAge(30 * 24 * 60 * 60);
        cookie1.setPath("/");
        cookie1.setDomain("zhxy.fjhnkj.com");
        response.addCookie(cookie);
        response.addCookie(cookie1);

        return jsonObject.toString();
    }


    @GetMapping("/token")
    @ApiOperation(value = "token刷新", notes = "token刷新")
    @ApiResponses({
            @ApiResponse(code = 401, message = "未携带token"),
            @ApiResponse(code = 412, message = "找不到用户")
    })
    public String getToken(@CookieValue(name = "RefreshToken") String refreshToken, HttpServletResponse response, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        if (refreshToken.isEmpty()) {
            response.setStatus(401);
            jsonObject.put("status", 0);
            jsonObject.put("msg", "请提供Token");
            return jsonObject.toString();
        }
        Long u = this.tokenService.verifyUser(refreshToken);
        if (u == -1) {
            response.setStatus(401);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "token过期");
            return jsonObject.toString();
        } else if (u == -2 || u == -4) {
            response.setStatus(401);
            jsonObject.put("status", -2);
            jsonObject.put("msg", "token错误");
            return jsonObject.toString();
        } else if (u == -3) {
            response.setStatus(401);
            jsonObject.put("status", -3);
            jsonObject.put("msg", "token签名错误");
        }
        UserPo user = this.userService.getById(u);
        if (user == null) {
            response.setStatus(412);
            jsonObject.put("status", -4);
            jsonObject.put("msg", "用户不存在");
            return jsonObject.toString();
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setPermissions(user.getPermissions());
        userDTO.setUsername(user.getUsername());
        userDTO.setAuth(user.getAuth());
        jsonObject.put("status", 1);
        jsonObject.put("msg", "获取成功");
        jsonObject.put("user", BeanMap.create(user));
        Cookie cookie = new Cookie("AccessToken", this.tokenService.getAccessToken(userDTO));
        cookie.setMaxAge(30 * 60);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setDomain("zhxy.fjhnkj.com");
        response.addCookie(cookie);
        return jsonObject.toString();
    }

    @PutMapping("/add")
    @ApiOperation(value = "添加用户", notes = "添加用户")
    @ApiResponses({
            @ApiResponse(code = 401, message = "未携带token"),
            @ApiResponse(code = 403, message = "权限不足"),
            @ApiResponse(code = 400, message = "密钥错误"),
            @ApiResponse(code = 500, message = "服务器异常")
    })
    public String addUser(@RequestBody @Valid UserLoginVo userLoginVo, BindingResult bindingResult, HttpServletResponse response, HttpServletRequest request) {
        for (ObjectError error : bindingResult.getAllErrors()) {
            return error.getDefaultMessage();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            userLoginVo.setPassword(this.rsaService.decrypt(userLoginVo.getPassword()));
        } catch (Exception e) {
            response.setStatus(400);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "请验证密钥是否正确");
            return jsonObject.toString();
        }
        UserPo user = new UserPo();
        user.setPassword(userLoginVo.getPassword());
        user.setUsername(userLoginVo.getUsername());
        if (this.userService.save(user)) {
            jsonObject.put("status", 1);
            jsonObject.put("msg", "添加成功");
        } else {
            response.setStatus(500);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "添加失败");
        }
        return jsonObject.toString();
    }

    @GetMapping("/list")
    @ApiOperation(value = "用户列表", notes = "用户列表")
    @ApiResponses({
            @ApiResponse(code = 401, message = "未携带token"),
            @ApiResponse(code = 403, message = "权限不足"),
            @ApiResponse(code = 412, message = "参数缺失或没有用户")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页页码", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "单页长度", required = true, dataType = "Integer", paramType = "query")
    })
    public String userList(@RequestParam Integer page, @RequestParam Integer size, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        if (page == null || size == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "参数不可为空");
            return jsonObject.toString();
        }
        Page<UserPo> users = this.userService.list(page, size);
        if (!users.getRecords().isEmpty()) {
            jsonObject.put("status", 1);
            jsonObject.put("msg", "获取成功");
            jsonObject.put("users", users.getRecords());
            jsonObject.put("pages", users.getPages());
        } else {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "获取失败");
        }
        return jsonObject.toString();
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除用户", notes = "删除用户")
    @ApiResponses({
            @ApiResponse(code = 401, message = "未携带token"),
            @ApiResponse(code = 403, message = "权限不足"),
            @ApiResponse(code = 412, message = "参数缺失或找不到用户")
    })
    public String deleteUser(@RequestParam Integer id, HttpServletResponse response, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        if (id == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "参数不可为空");
            return jsonObject.toString();
        }
        if (this.userService.removeById(id)) {
            jsonObject.put("status", 1);
            jsonObject.put("msg", "删除成功");
        } else {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "未找到用户");
        }
        return jsonObject.toString();
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "更新用户信息", notes = "更新用户信息")
    @ApiResponses({
            @ApiResponse(code = 401, message = "未携带token"),
            @ApiResponse(code = 400, message = "密钥错误"),
            @ApiResponse(code = 403, message = "权限不足"),
            @ApiResponse(code = 412, message = "找不到用户")
    })
    public String updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserLoginVo userLoginVo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
        for (ObjectError error : bindingResult.getAllErrors()) {
            return error.getDefaultMessage();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            userLoginVo.setPassword(this.rsaService.decrypt(userLoginVo.getPassword()));
        } catch (Exception e) {
            response.setStatus(400);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "请验证密钥是否正确");
            return jsonObject.toString();
        }
        UserPo user = new UserPo();
        user.setPassword(userLoginVo.getPassword());
        user.setUsername(userLoginVo.getUsername());
        user.setId(id);
        if (this.userService.updateById(user)) {
            jsonObject.put("status", 1);
            jsonObject.put("msg", "更新成功");
        } else {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "更新失败");
        }
        return jsonObject.toString();
    }

    @GetMapping("/get")
    @ApiOperation(value = "获取用户", notes = "获取单个用户")
    @ApiResponses({
            @ApiResponse(code = 401, message = "未携带token"),
            @ApiResponse(code = 403, message = "权限与操作不符"),
            @ApiResponse(code = 412, message = "参数缺失或找不到用户")
    })
    public String getUser(@RequestParam Integer id, HttpServletResponse response, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        if (id == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "参数不可为空");
            return jsonObject.toString();
        }
        int permissions = (int) request.getAttribute("permissions");
        UserPo user = this.userService.getById(id);
        if (user == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "未查询到用户");
            return jsonObject.toString();
        }
        if (!user.getId().equals(tokenService.verifyUser(request.getHeader("AccessToken"))) && permissions < 2) {
            response.setStatus(403);
            jsonObject.put("status", 0);
            jsonObject.put("msg", "权限不足");
            return jsonObject.toString();
        }
        jsonObject.put("status", 1);
        jsonObject.put("msg", "获取成功");
        jsonObject.put("user", BeanMap.create(user));
        return jsonObject.toString();
    }

    @GetMapping("/select")
    @ApiOperation(value = "查询用户", notes = "查询用户")
    @ApiResponses({
            @ApiResponse(code = 401, message = "未携带token"),
            @ApiResponse(code = 403, message = "权限不足"),
            @ApiResponse(code = 412, message = "参数缺失或找不到用户")
    })
    public String selectUser(@RequestParam String key, @RequestParam Integer page, @RequestParam Integer size, HttpServletResponse response, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        if (key == null || page == null || size == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "参数不可为空");
            return jsonObject.toString();
        }
        Page<UserPo> users = this.userService.select(key, page, size);
        jsonObject.put("status", 1);
        jsonObject.put("msg", "获取成功");
        jsonObject.put("users", users.getRecords());
        jsonObject.put("pages", users.getPages());
        return jsonObject.toString();
    }

}

