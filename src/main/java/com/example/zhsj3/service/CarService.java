package com.example.zhsj3.service;

import com.example.zhsj3.entity.Car;

import java.util.List;

public interface CarService {

    List<Car> getAllCars();

    Car getCarById(Long id);

    List<Car> searchCars(String keyword);
}