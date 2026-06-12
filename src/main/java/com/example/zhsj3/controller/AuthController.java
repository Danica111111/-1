package com.example.zhsj3.controller;

import com.example.zhsj3.entity.User;
import com.example.zhsj3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        User user = userService.login(username, password);
        Map<String, Object> result = new HashMap<>();
        if (user != null) {
            result.put("code", 200);
            result.put("message", "登录成功");
            result.put("data", user);
        } else {
            result.put("code", 400);
            result.put("message", "用户名或密码错误");
        }
        return result;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        boolean success = userService.register(user);
        Map<String, Object> result = new HashMap<>();
        if (success) {
            result.put("code", 200);
            result.put("message", "注册成功");
        } else {
            result.put("code", 400);
            result.put("message", "用户名已存在或注册失败");
        }
        return result;
    }

    @PutMapping("/password")
    public Map<String, Object> updatePassword(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String newPassword = params.get("password");
        boolean success = userService.updatePassword(username, newPassword);
        Map<String, Object> result = new HashMap<>();
        if (success) {
            result.put("code", 200);
            result.put("message", "修改成功");
        } else {
            result.put("code", 400);
            result.put("message", "修改失败");
        }
        return result;
    }

    @PostMapping("/forget-password")
    public Map<String, Object> forgetPassword(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String newPassword = params.get("password");
        boolean success = userService.updatePassword(username, newPassword);
        Map<String, Object> result = new HashMap<>();
        if (success) {
            result.put("code", 200);
            result.put("message", "找回密码成功");
        } else {
            result.put("code", 400);
            result.put("message", "找回密码失败");
        }
        return result;
    }
}