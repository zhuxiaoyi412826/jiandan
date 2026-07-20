package com.example.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.mall.entity.User;

public interface IUserService extends IService<User> {
    User login(String username, String password);
}
