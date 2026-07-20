package com.example.mall.controller;

import com.example.mall.common.Result;
import com.example.mall.dto.LoginDTO;
import com.example.mall.entity.User;
import com.example.mall.service.IUserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户管理", description = "用户的登录、注册、查询、修改、删除接口")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "用户登录", description = "根据用户名和密码进行登录认证")
    @PostMapping("/login")
    public Result<User> login(@RequestBody LoginDTO loginDTO) {
        User user = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
        if (user != null) {
            return Result.success(user);
        }
        return Result.error(401, "用户名或密码错误");
    }

    @Operation(summary = "用户注册", description = "注册新用户，用户名不能重复")
    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        if (userService.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, user.getUsername())) != null) {
            return Result.error(400, "用户名已存在");
        }
        userService.save(user);
        return Result.success("注册成功", user);
    }

    @Operation(summary = "获取所有用户", description = "返回系统中所有用户的列表")
    @GetMapping
    public Result<List<User>> getAllUsers() {
        return Result.success(userService.list());
    }

    @Operation(summary = "根据ID查询用户", description = "根据用户ID获取用户详细信息")
    @GetMapping("/{id}")
    public Result<User> getUserById(@Parameter(description = "用户ID") @PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    @Operation(summary = "更新用户信息", description = "根据用户ID更新用户信息")
    @PutMapping("/{id}")
    public Result<User> updateUser(@Parameter(description = "用户ID") @PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userService.updateById(user);
        return Result.success("更新成功", userService.getById(id));
    }

    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@Parameter(description = "用户ID") @PathVariable Long id) {
        userService.removeById(id);
        return Result.success(null);
    }

}
