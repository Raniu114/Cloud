package org.raniu.user.service.impl;

import cn.hutool.Hutool;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.raniu.api.dto.UserDTO;
import org.raniu.api.enums.ResultCode;
import org.raniu.api.vo.Result;
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

import java.util.List;

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
            queryWrapper.eq("username", userLoginVo.getUsername()).ge("permissions", 1);
        } else {
            queryWrapper.eq("username", userLoginVo.getUsername()).lt("permissions", 1);
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
    public Result<UserDTO> loginAdmin(UserLoginVo userLoginVo, HttpServletResponse response) {
        try {
            userLoginVo.setPassword(this.rsaService.decrypt(userLoginVo.getPassword()));
            UserPo user = login(userLoginVo, 1);
            if (user == null) {
                response.setStatus(412);
                return Result.error(ResultCode.ERROR_PARAMETERS, "用户不存在");
            }
            if (!this.rsaService.decrypt(user.getPassword()).equals(userLoginVo.getPassword())) {
                return Result.error(ResultCode.ERROR_PARAMETERS, "密码错误");
            }
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setPermissions(user.getPermissions());
            userDTO.setUsername(user.getUsername());
            userDTO.setAuth(JSONUtil.parseObj(user.getAuth()));
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
            Cookie cookie2 = new Cookie("user_id", userDTO.getId().toString());
            cookie2.setMaxAge(30 * 24 * 60 * 60);
            cookie2.setPath("/");
            cookie2.setDomain("zhxy.fjhnkj.com");
            response.addCookie(cookie);
            response.addCookie(cookie1);
            response.addCookie(cookie2);
            return Result.success("登陆成功", userDTO);
        } catch (Exception e) {
            response.setStatus(400);
            return Result.error(ResultCode.ERROR_PARAMETERS, "请验证密钥是否正确");
        }

    }

    @Override
    public Result<UserDTO> loginUser(UserLoginVo userLoginVo, HttpServletResponse response) {
        try {
            userLoginVo.setPassword(this.rsaService.decrypt(userLoginVo.getPassword()));
            UserPo user = login(userLoginVo, 0);
            if (user == null) {
                response.setStatus(412);
                return Result.error(ResultCode.ERROR_PARAMETERS, "用户不存在");
            }
            if (!this.rsaService.decrypt(user.getPassword()).equals(userLoginVo.getPassword())) {
                return Result.error(ResultCode.ERROR_PARAMETERS, "密码错误");
            }
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setPermissions(user.getPermissions());
            userDTO.setUsername(user.getUsername());
            userDTO.setAuth(JSONUtil.parseObj(user.getAuth()));
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
            Cookie cookie2 = new Cookie("user_id", userDTO.getId().toString());
            cookie2.setMaxAge(30 * 24 * 60 * 60);
            cookie2.setPath("/");
            cookie2.setDomain("zhxy.fjhnkj.com");
            response.addCookie(cookie);
            response.addCookie(cookie1);
            response.addCookie(cookie2);
            return Result.success("登陆成功", userDTO);
        } catch (Exception e) {
            response.setStatus(400);
            return Result.error(ResultCode.ERROR_PARAMETERS, "请验证密钥是否正确");
        }
    }

    @Override
    public Result<UserDTO> getToken(String refreshToken, HttpServletResponse response) {
        if (refreshToken == null) {
            response.setStatus(401);
            return Result.error(ResultCode.TOKEN_TIMEOUT, "请提供Token");
        }
        Long u = this.tokenService.verifyUser(refreshToken);
        if (u == -1) {
            response.setStatus(401);
            return Result.error(ResultCode.TOKEN_TIMEOUT, "token过期");
        } else if (u == -2 || u == -3 || u == -4) {
            response.setStatus(401);
            return Result.error(ResultCode.TOKEN_ERROR, "token错误");
        }
        UserPo user = getById(u);
        if (user == null) {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "用户不存在");
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setPermissions(user.getPermissions());
        userDTO.setUsername(user.getUsername());
        userDTO.setAuth(JSONUtil.parseObj(user.getAuth()));
        Cookie cookie = new Cookie("AccessToken", this.tokenService.getAccessToken(userDTO));
        cookie.setMaxAge(30 * 60);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setDomain("zhxy.fjhnkj.com");
        response.addCookie(cookie);
        return Result.success("刷新成功", userDTO);
    }

    @Override
    public Result<UserVo> addUser(UserVo userVo, HttpServletResponse response) {
        try {
            this.rsaService.decrypt(userVo.getPassword());
        } catch (Exception e) {
            response.setStatus(400);
            return Result.error(ResultCode.ERROR_PARAMETERS, "请验证密钥是否正确");
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
        user.setAuth(userVo.getAuth());
        if (save(user)) {
            return Result.success("添加成功");
        } else {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS,"添加失败");
        }
    }

    @Override
    public Result<List<UserDTO>> userList(Integer page, Integer size, HttpServletResponse response) {
        if (page == null || size == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING,"参数不可为空");
        }
        Page<UserPo> users = list(page, size);
        List<UserDTO> userDTOList = JSONUtil.toList(JSONUtil.toJsonStr(users.getRecords()), UserDTO.class);
        if (!users.getRecords().isEmpty()) {
            return Result.success(userDTOList,users.getPages());
        } else {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "获取失败");
        }
    }

    @Override
    public Result<UserVo> delUser(Long id, HttpServletResponse response) {
        if (id == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING,"参数不可为空");
        }
        if (removeById(id)) {
            return Result.success("删除成功");
        } else {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "未找到用户");
        }
    }

    @Override
    public Result<UserVo> updateUser(UserVo userVo, HttpServletResponse response) {
        try {
            userVo.setPassword(this.rsaService.decrypt(userVo.getPassword()));
        } catch (Exception e) {
            response.setStatus(400);
            return Result.error(ResultCode.ERROR_PARAMETERS, "请验证密钥是否正确");
        }
        UserPo user = new UserPo();
        user.setPassword(userVo.getPassword());
        user.setUsername(userVo.getUsername());
        user.setId(userVo.getId());
        user.setEmail(userVo.getEmail());
        user.setPhone(userVo.getPhone());
        user.setAuth(userVo.getAuth());
        user.setAddr(userVo.getAddr());
        user.setPermissions(userVo.getPermissions());
        if (updateById(user)) {
            return Result.success("更新成功");
        } else {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "更新失败");
        }
    }

    @Override
    public Result<UserDTO> getUser(Long id, HttpServletResponse response) {
        if (id == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING,"参数不可为空");
        }
        UserPo user = getById(id);
        if (user == null) {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "未找到用户");
        }
        return Result.success(JSONUtil.toBean(JSONUtil.toJsonStr(user), UserDTO.class));
    }

    @Override
    public Result<List<UserDTO>> selectUser(String key, Integer page, Integer size, HttpServletResponse response) {
        if (key == null || page == null || size == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING,"参数不可为空");
        }
        Page<UserPo> users = select(key, page, size);
        List<UserDTO> userDTOList = JSONUtil.toList(JSONUtil.toJsonStr(users.getRecords()), UserDTO.class);
        return Result.success(userDTOList,users.getPages());
    }

}
