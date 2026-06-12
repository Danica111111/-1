package com.example.zhsj3.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.zhsj3.entity.Car;
import com.example.zhsj3.mapper.CarMapper;
import com.example.zhsj3.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarMapper carMapper;

    @Override
    public List<Car> getAllCars() {
        return carMapper.selectList(null);
    }

    @Override
    public Car getCarById(Long id) {
        return carMapper.selectById(id);
    }

    @Override
    public List<Car> searchCars(String keyword) {
        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();

        queryWrapper.like("brand", keyword)
                .or()
                .like("model", keyword)
                .or()
                .like("car_type", keyword);

        return carMapper.selectList(queryWrapper);
    }
}