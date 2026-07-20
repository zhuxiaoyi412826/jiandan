package com.example.mall.controller;

import com.example.mall.common.Result;
import com.example.mall.dto.LoginDTO;
import com.example.mall.entity.User;
import com.example.mall.service.IUserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<User> login(@RequestBody LoginDTO loginDTO) {
        User user = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
        if (user != null) {
            return Result.success(user);
        }
        return Result.error(401, "用户名或密码错误");
    }

    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        if (userService.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, user.getUsername())) != null) {
            return Result.error(400, "用户名已存在");
        }
        userService.save(user);
        return Result.success("注册成功", user);
    }

    @GetMapping
    public Result<List<User>> getAllUsers() {
        return Result.success(userService.list());
    }

    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    @PutMapping("/{id}")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userService.updateById(user);
        return Result.success("更新成功", userService.getById(id));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success(null);
    }

}
