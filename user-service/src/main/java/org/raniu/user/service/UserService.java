package org.raniu.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.raniu.user.domain.po.UserPo;
import org.raniu.user.domain.vo.UserLoginVo;
import org.raniu.user.domain.vo.UserVo;


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

    String loginAdmin(UserLoginVo userLoginVo, HttpServletResponse response);

    String loginUser(UserLoginVo userLoginVo, HttpServletResponse response);

    String getToken(String refreshToken, HttpServletResponse response);

    String addUser(UserVo userVo, HttpServletResponse response);

    String userList(Integer page,Integer size,HttpServletResponse response);

    String delUser(Integer id,HttpServletResponse response);

    String updateUser(UserVo userVo, HttpServletResponse response);

    String getUser(Long id, HttpServletResponse response);

    String selectUser(String key, Integer page, Integer size, HttpServletResponse response);
}
