package com.notayessir.user.user.service;

import com.notayessir.user.user.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IUserService extends IService<User> {

    User findByUsername(String username);

    void createUser(User user);

    User findById(long id);

}
