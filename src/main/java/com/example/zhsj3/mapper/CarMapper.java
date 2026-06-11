package com.example.zhsj3.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zhsj3.entity.Car;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CarMapper extends BaseMapper<Car> {
}