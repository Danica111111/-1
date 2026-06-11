package com.example.zhsj3.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.zhsj3.common.Result;
import com.example.zhsj3.dto.AiRecommendDTO;
import com.example.zhsj3.entity.Car;
import com.example.zhsj3.mapper.CarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin
public class AiController {

    @Autowired
    private CarMapper carMapper;

    /**
     * AI 智能选车助手
     */
    @PostMapping("/recommend")
    public Result<String> recommendCar(@RequestBody AiRecommendDTO aiRecommendDTO) {

        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "AVAILABLE");

        List<Car> availableCars = carMapper.selectList(queryWrapper);

        if (availableCars == null || availableCars.isEmpty()) {
            return Result.error("当前没有可用车辆");
        }

        Car bestCar = availableCars.get(0);

        String result =
                "用户需求：" + aiRecommendDTO.getRequirement() + "\n\n" +
                        "推荐车辆：" + bestCar.getBrand() + " " + bestCar.getModel() + "\n" +
                        "车型类型：" + bestCar.getCarType() + "\n" +
                        "座位数：" + bestCar.getSeats() + " 座\n" +
                        "日租金：" + bestCar.getDailyPrice() + " 元/天\n\n" +
                        "推荐理由：\n" +
                        "1. 该车辆当前处于可租状态，可以直接下单。\n" +
                        "2. 该车型价格适中，适合普通用户租车出行。\n" +
                        "3. 车辆座位数和车型信息可以满足大部分日常出行、家庭出游或短途旅行需求。\n\n" +
                        "说明：当前为 AI 智能选车助手的基础版本，后续可接入大模型 API，根据用户自然语言需求进行更精准推荐。";

        return Result.success(result);
    }
}