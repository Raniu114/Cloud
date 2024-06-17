package org.raniu.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.raniu.api.dto.UserDTO;
import org.raniu.api.vo.Result;
import org.raniu.user.domain.po.UserPo;
import org.raniu.user.domain.vo.UserLoginVo;
import org.raniu.user.domain.vo.UserVo;

import java.util.List;


/**
 * <p>
 * 服务类
 * </p>
 *
 * @author raniu
 * @since 2023-12-11
 */
public interface UserService extends IService<UserPo> {
    UserPo login(UserLoginVo userLoginVo,Integer permission);

    Page<UserPo> list(Integer page, Integer size);

    Page<UserPo> select(String key, Integer page, Integer size);

    Result<UserDTO> loginAdmin(UserLoginVo userLoginVo, HttpServletResponse response);

    Result<UserDTO> loginUser(UserLoginVo userLoginVo, HttpServletResponse response);

    Result<UserDTO> getToken(String refreshToken, HttpServletResponse response);

    Result<UserVo> addUser(UserVo userVo, HttpServletResponse response);

    Result<List<UserPo>> userList(Integer page, Integer size, HttpServletResponse response);

    Result<UserVo> delUser(Long id,HttpServletResponse response);

    Result<UserVo> updateUser(UserVo userVo, HttpServletResponse response);

    Result<UserPo> getUser(Long id, HttpServletResponse response);

    Result<List<UserPo>> selectUser(String key, Integer page, Integer size, HttpServletResponse response);
}
