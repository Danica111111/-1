package com.example.zhsj3.service;

import com.example.zhsj3.entity.User;

public interface UserService {

    boolean register(User user);

    User login(String username, String password);

    boolean updatePassword(String username, String newPassword);

    User getUserById(Long id);

    boolean updateUserInfo(User user);
}