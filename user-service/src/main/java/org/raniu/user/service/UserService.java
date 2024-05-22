package org.raniu.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.raniu.user.domain.po.UserPo;
import org.raniu.user.domain.vo.UserLoginVo;


/**
 * <p>
 * 服务类
 * </p>
 *
 * @author raniu
 * @since 2023-12-11
 */
public interface UserService extends IService<UserPo> {
    UserPo login(UserLoginVo userLoginVo);

    Page<UserPo> list(Integer page, Integer size);

    Page<UserPo> select(String key, Integer page, Integer size);
}
