package com.notayessir.user.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.notayessir.common.constant.EnumFieldDeleted;
import com.notayessir.user.user.entity.User;
import com.notayessir.user.user.mapper.UserMapper;
import com.notayessir.user.user.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public User findByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(User::getUsername, username);
        return getOne(queryWrapper);
    }

    @Override
    public void createUser(User user) {
        save(user);
    }

    @Override
    public User findById(long id) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(User::getId, id);
        return getOne(queryWrapper);
    }

}
