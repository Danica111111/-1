package com.example.zhsj3.controller;

import com.example.zhsj3.entity.User;
import com.example.zhsj3.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public Map<String, Object> getUserInfo(@PathVariable("userId") Long userId) {
        User user = userService.getUserById(userId);
        Map<String, Object> result = new HashMap<>();
        if (user != null) {
            result.put("code", 200);
            result.put("data", user);
        } else {
            result.put("code", 404);
            result.put("message", "未找到该用户");
        }
        return result;
    }

    @PutMapping("/{userId}")
    public Map<String, Object> updateUserInfo(@PathVariable("userId") Long userId, @RequestBody User user) {
        user.setId(userId);
        boolean success = userService.updateUserInfo(user);
        Map<String, Object> result = new HashMap<>();
        if (success) {
            result.put("code", 200);
            result.put("message", "更新成功");
        } else {
            result.put("code", 400);
            result.put("message", "更新失败");
        }
        return result;
    }
}