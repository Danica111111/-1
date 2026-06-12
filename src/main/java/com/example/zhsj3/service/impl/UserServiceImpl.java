package com.example.zhsj3.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.zhsj3.entity.User;
import com.example.zhsj3.mapper.UserMapper;
import com.example.zhsj3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean register(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());

        User existUser = userMapper.selectOne(queryWrapper);

        if (existUser != null) {
            return false;
        }

        return userMapper.insert(user) > 0;
    }

    @Override
    public User login(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);

        User user = userMapper.selectOne(queryWrapper);

        if (user != null && password.equals(user.getPassword())) {
            return user;
        }

        return null;
    }

    @Override
    public boolean updatePassword(String username, String newPassword) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("username", username)
                .set("password", newPassword);

        return userMapper.update(null, updateWrapper) > 0;
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public boolean updateUserInfo(User user) {
        return userMapper.updateById(user) > 0;
    }
}