package org.raniu.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.raniu.api.dto.UserDTO;
import org.raniu.common.utils.UserContext;
import org.raniu.user.domain.po.UserPo;
import org.raniu.user.domain.vo.UserLoginVo;
import org.raniu.user.domain.vo.UserVo;
import org.raniu.user.mapper.UserMapper;
import org.raniu.user.service.RSAService;
import org.raniu.user.service.TokenService;
import org.raniu.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author raniu
 * @since 2023-12-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPo> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RSAService rsaService;

    @Autowired
    private TokenService tokenService;

    @Override
    public UserPo login(UserLoginVo userLoginVo, Integer permission) {
        QueryWrapper<UserPo> queryWrapper = new QueryWrapper<UserPo>();
        if (permission == 1) {
            queryWrapper.eq("username", userLoginVo.getUsername()).eq("password", userLoginVo.getPassword()).ge("permission", 1);
        } else {
            queryWrapper.eq("username", userLoginVo.getUsername()).eq("password", userLoginVo.getPassword()).lt("permission", 1);
        }
        return this.userMapper.selectOne(queryWrapper);
    }

    @Override
    public Page<UserPo> list(Integer page, Integer size) {
        Page<UserPo> userPage = new Page<>(page, size);
        QueryWrapper<UserPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("permissions", 2).select(UserPo.class, info -> !"password".equals(info.getColumn()));
        return this.userMapper.selectPage(userPage, queryWrapper);
    }

    @Override
    public Page<UserPo> select(String key, Integer page, Integer size) {
        Page<UserPo> userPage = new Page<>(page, size);
        QueryWrapper<UserPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", key).select(UserPo.class, info -> !"password".equals(info.getColumn()));
        return this.userMapper.selectPage(userPage, queryWrapper);
    }

    @Override
    public String loginAdmin(UserLoginVo userLoginVo, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            userLoginVo.setPassword(this.rsaService.decrypt(userLoginVo.getPassword()));
        } catch (Exception e) {
            response.setStatus(400);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "请验证密钥是否正确");
            return jsonObject.toString();
        }
        UserPo user = login(userLoginVo, 1);
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

    @Override
    public String loginUser(UserLoginVo userLoginVo, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            userLoginVo.setPassword(this.rsaService.decrypt(userLoginVo.getPassword()));
        } catch (Exception e) {
            response.setStatus(400);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "请验证密钥是否正确");
            return jsonObject.toString();
        }
        UserPo user = login(userLoginVo, 0);
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
        cookie.setHttpOnly(true);
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

    @Override
    public String getToken(String refreshToken, HttpServletResponse response) {
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
        UserPo user = getById(u);
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
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setDomain("zhxy.fjhnkj.com");
        response.addCookie(cookie);
        return jsonObject.toString();
    }

    @Override
    public String addUser(UserVo userVo, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            userVo.setPassword(this.rsaService.decrypt(userVo.getPassword()));
        } catch (Exception e) {
            response.setStatus(400);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "请验证密钥是否正确");
            return jsonObject.toString();
        }
        UserPo user = new UserPo();
        user.setPassword(userVo.getPassword());
        user.setUsername(userVo.getUsername());
        user.setCreatUser(UserContext.getUser());
        user.setPermissions(userVo.getPermissions());
        user.setAddr(userVo.getAddr());
        user.setEmail(userVo.getEmail());
        user.setPhone(userVo.getPhone());
        user.setCreatTime(System.currentTimeMillis());
        if (save(user)) {
            jsonObject.put("status", 1);
            jsonObject.put("msg", "添加成功");
        } else {
            response.setStatus(500);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "添加失败");
        }
        return jsonObject.toString();
    }

    @Override
    public String userList(Integer page, Integer size, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        if (page == null || size == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "参数不可为空");
            return jsonObject.toString();
        }
        Page<UserPo> users = list(page, size);
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

    @Override
    public String delUser(Integer id, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        if (id == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "参数不可为空");
            return jsonObject.toString();
        }
        if (removeById(id)) {
            jsonObject.put("status", 1);
            jsonObject.put("msg", "删除成功");
        } else {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "未找到用户");
        }
        return jsonObject.toString();
    }

    @Override
    public String updateUser( UserVo userVo, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            userVo.setPassword(this.rsaService.decrypt(userVo.getPassword()));
        } catch (Exception e) {
            response.setStatus(400);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "请验证密钥是否正确");
            return jsonObject.toString();
        }
        UserPo user = new UserPo();
        user.setPassword(userVo.getPassword());
        user.setUsername(userVo.getUsername());
        user.setId(user.getId());
        user.setEmail(userVo.getEmail());
        user.setPhone(userVo.getPhone());
        user.setAuth(userVo.getAuth());
        user.setAddr(userVo.getAddr());
        user.setPermissions(userVo.getPermissions());
        if (updateById(user)) {
            jsonObject.put("status", 1);
            jsonObject.put("msg", "更新成功");
        } else {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "更新失败");
        }
        return jsonObject.toString();
    }

    @Override
    public String getUser(Long id, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        if (id == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "参数不可为空");
            return jsonObject.toString();
        }

        UserPo user = getById(id);
        if (user == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "未查询到用户");
            return jsonObject.toString();
        }
        if (!user.getId().equals(UserContext.getUser()) && UserContext.getPermissions() < 2) {
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

    @Override
    public String selectUser(String key, Integer page, Integer size, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        if (key == null || page == null || size == null) {
            response.setStatus(412);
            jsonObject.put("status", -1);
            jsonObject.put("msg", "参数不可为空");
            return jsonObject.toString();
        }
        Page<UserPo> users = select(key, page, size);
        jsonObject.put("status", 1);
        jsonObject.put("msg", "获取成功");
        jsonObject.put("users", users.getRecords());
        jsonObject.put("pages", users.getPages());
        return jsonObject.toString();
    }

}
