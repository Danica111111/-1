package com.example.zhsj3.controller;

import com.example.zhsj3.common.Result;
import com.example.zhsj3.dto.RentCarDTO;
import com.example.zhsj3.dto.ReserveCarDTO;
import com.example.zhsj3.entity.RentalOrder;
import com.example.zhsj3.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 租赁汽车
     */
    @PostMapping("/rent")
    public Result<Void> rentCar(@RequestBody RentCarDTO rentCarDTO) {
        return orderService.rentCar(rentCarDTO);
    }

    /**
     * 预约租车
     */
    @PostMapping("/reserve")
    public Result<Void> reserveCar(@RequestBody ReserveCarDTO reserveCarDTO) {
        return orderService.reserveCar(reserveCarDTO);
    }

    /**
     * 归还车辆
     */
    @PostMapping("/return/{orderId}")
    public Result<RentalOrder> returnCar(@PathVariable Long orderId) {
        return orderService.returnCar(orderId);
    }

    /**
     * 支付押金
     */
    @PostMapping("/pay-deposit/{orderId}")
    public Result<Void> payDeposit(@PathVariable Long orderId) {
        return orderService.payDeposit(orderId);
    }

    /**
     * 查询某个用户的订单
     */
    @GetMapping("/user/{userId}")
    public Result<List<RentalOrder>> getUserOrders(@PathVariable Long userId) {
        return orderService.getUserOrders(userId);
    }



    /**
     * 取消预约
     */
    @PostMapping("/cancel-reserve/{orderId}")
    public Result<Void> cancelReserve(@PathVariable Long orderId) {
        return orderService.cancelReserve(orderId);
    }
}