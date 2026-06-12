package com.example.zhsj3.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zhsj3.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}