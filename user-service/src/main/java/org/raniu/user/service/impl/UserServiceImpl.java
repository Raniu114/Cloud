package org.raniu.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.raniu.user.domain.po.UserPo;
import org.raniu.user.domain.vo.UserLoginVo;
import org.raniu.user.mapper.UserMapper;
import org.raniu.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public UserPo login(UserLoginVo userLoginVo) {
        QueryWrapper<UserPo> queryWrapper = new QueryWrapper<UserPo>();
        queryWrapper.eq("username", userLoginVo.getUsername()).eq("password", userLoginVo.getPassword());
        return this.userMapper.selectOne(queryWrapper);
    }

    @Override
    public Page<UserPo> list(Integer page, Integer size) {
        Page<UserPo> userPage = new Page<>(page, size);
        QueryWrapper<UserPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("permissions", 2);
        return this.userMapper.selectPage(userPage, queryWrapper);
    }

    @Override
    public Page<UserPo> select(String key, Integer page, Integer size) {
        Page<UserPo> userPage = new Page<>(page, size);
        QueryWrapper<UserPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", key);
        return this.userMapper.selectPage(userPage, queryWrapper);
    }

}
