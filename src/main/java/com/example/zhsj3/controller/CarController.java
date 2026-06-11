package com.example.zhsj3.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.zhsj3.common.Result;
import com.example.zhsj3.entity.Car;
import com.example.zhsj3.mapper.CarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@CrossOrigin
public class CarController {

    @Autowired
    private CarMapper carMapper;

    /**
     * 查询所有车辆
     */
    @GetMapping
    public Result<List<Car>> listCars() {
        List<Car> cars = carMapper.selectList(null);
        return Result.success(cars);
    }

    /**
     * 根据 ID 查询车辆
     */
    @GetMapping("/{carId}")
    public Result<Car> getCarById(@PathVariable Long carId) {
        Car car = carMapper.selectById(carId);

        if (car == null) {
            return Result.error("车辆不存在");
        }

        return Result.success(car);
    }

    /**
     * 搜索车辆
     */
    @GetMapping("/search")
    public Result<List<Car>> searchCars(@RequestParam String keyword) {
        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();

        queryWrapper.like("brand", keyword)
                .or()
                .like("model", keyword)
                .or()
                .like("car_type", keyword);

        List<Car> cars = carMapper.selectList(queryWrapper);

        return Result.success(cars);
    }
}