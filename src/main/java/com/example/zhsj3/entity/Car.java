package com.example.zhsj3.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("car")
public class Car {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String brand;

    private String model;

    private String carType;

    private Integer seats;

    private BigDecimal dailyPrice;

    private String imageUrl;

    private String status;

    private String description;
}