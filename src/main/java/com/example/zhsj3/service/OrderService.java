package com.example.zhsj3.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.zhsj3.common.Result;
import com.example.zhsj3.dto.RentCarDTO;
import com.example.zhsj3.dto.ReserveCarDTO;
import com.example.zhsj3.entity.Car;
import com.example.zhsj3.entity.RentalOrder;
import com.example.zhsj3.mapper.CarMapper;
import com.example.zhsj3.mapper.RentalOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private CarMapper carMapper;

    @Autowired
    private RentalOrderMapper rentalOrderMapper;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 租赁汽车
     */
    @Transactional
    public Result<Void> rentCar(RentCarDTO rentCarDTO) {
        Car car = carMapper.selectById(rentCarDTO.getCarId());

        if (car == null) {
            return Result.error("车辆不存在");
        }

        if (!"AVAILABLE".equals(car.getStatus())) {
            return Result.error("该车辆当前不可租赁");
        }

        RentalOrder order = new RentalOrder();
        order.setUserId(rentCarDTO.getUserId());
        order.setCarId(rentCarDTO.getCarId());
        order.setStartTime(parseTimeOrNow(rentCarDTO.getStartTime()));
        order.setExpectedReturnTime(parseTimeOrDefault(rentCarDTO.getExpectedReturnTime()));
        order.setDeposit(new BigDecimal("500"));
        order.setTotalFee(null);
        order.setPickupLocation(rentCarDTO.getPickupLocation());
        order.setReturnLocation(rentCarDTO.getReturnLocation());
        order.setStatus("RENTING");
        order.setCreateTime(LocalDateTime.now());

        rentalOrderMapper.insert(order);

        car.setStatus("RENTED");
        carMapper.updateById(car);

        return Result.success("租车成功，请支付押金", null);
    }

    /**
     * 预约租车
     */
    @Transactional
    public Result<Void> reserveCar(ReserveCarDTO reserveCarDTO) {
        Car car = carMapper.selectById(reserveCarDTO.getCarId());

        if (car == null) {
            return Result.error("车辆不存在");
        }

        QueryWrapper<RentalOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("car_id", reserveCarDTO.getCarId())
                .eq("status", "RESERVED");

        Long count = rentalOrderMapper.selectCount(queryWrapper);

        if (count != null && count > 0) {
            return Result.error("该车辆已被预约，不能重复预约");
        }

        RentalOrder order = new RentalOrder();
        order.setUserId(reserveCarDTO.getUserId());
        order.setCarId(reserveCarDTO.getCarId());
        order.setStartTime(parseTimeOrNow(reserveCarDTO.getReserveTime()));
        order.setExpectedReturnTime(null);
        order.setActualReturnTime(null);
        order.setDeposit(new BigDecimal("500"));
        order.setTotalFee(null);
        order.setPickupLocation(reserveCarDTO.getPickupLocation());
        order.setReturnLocation(reserveCarDTO.getReturnLocation());
        order.setStatus("RESERVED");
        order.setCreateTime(LocalDateTime.now());

        rentalOrderMapper.insert(order);

        car.setStatus("RESERVED");
        carMapper.updateById(car);

        return Result.success("预约成功", null);
    }

    /**
     * 取消预约
     */
    @Transactional
    public Result<Void> cancelReserve(Long orderId) {
        RentalOrder order = rentalOrderMapper.selectById(orderId);

        if (order == null) {
            return Result.error("订单不存在");
        }

        if (!"RESERVED".equals(order.getStatus())) {
            return Result.error("只有预约中的订单才能取消");
        }

        Car car = carMapper.selectById(order.getCarId());

        if (car == null) {
            return Result.error("车辆不存在");
        }

        order.setStatus("CANCELLED");
        rentalOrderMapper.updateById(order);

        car.setStatus("AVAILABLE");
        carMapper.updateById(car);

        return Result.success("取消预约成功", null);
    }

    /**
     * 归还车辆
     */
    @Transactional
    public Result<RentalOrder> returnCar(Long orderId) {
        RentalOrder order = rentalOrderMapper.selectById(orderId);

        if (order == null) {
            return Result.error("订单不存在");
        }

        if (!"RENTING".equals(order.getStatus())) {
            return Result.error("该订单不是租赁中状态，不能归还");
        }

        Car car = carMapper.selectById(order.getCarId());

        if (car == null) {
            return Result.error("车辆不存在");
        }

        LocalDateTime now = LocalDateTime.now();

        long days = ChronoUnit.DAYS.between(order.getStartTime(), now);

        if (days <= 0) {
            days = 1;
        }

        BigDecimal totalFee = car.getDailyPrice().multiply(new BigDecimal(days));

        order.setActualReturnTime(now);
        order.setTotalFee(totalFee);
        order.setStatus("FINISHED");

        rentalOrderMapper.updateById(order);

        car.setStatus("AVAILABLE");
        carMapper.updateById(car);

        return Result.success("归还成功，本次费用为 " + totalFee + " 元", order);
    }

    /**
     * 支付押金
     */
    public Result<Void> payDeposit(Long orderId) {
        RentalOrder order = rentalOrderMapper.selectById(orderId);

        if (order == null) {
            return Result.error("订单不存在");
        }

        return Result.success("押金支付成功", null);
    }

    /**
     * 查询某个用户的订单
     */
    public Result<List<RentalOrder>> getUserOrders(Long userId) {
        QueryWrapper<RentalOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .orderByDesc("create_time");

        List<RentalOrder> orders = rentalOrderMapper.selectList(queryWrapper);

        return Result.success(orders);
    }

    /**
     * 查询所有订单
     */
    public Result<List<RentalOrder>> listOrders() {
        QueryWrapper<RentalOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");

        List<RentalOrder> orders = rentalOrderMapper.selectList(queryWrapper);

        return Result.success(orders);
    }

    private LocalDateTime parseTimeOrNow(String time) {
        if (time == null || time.trim().isEmpty()) {
            return LocalDateTime.now();
        }

        return LocalDateTime.parse(time, FORMATTER);
    }

    private LocalDateTime parseTimeOrDefault(String time) {
        if (time == null || time.trim().isEmpty()) {
            return LocalDateTime.now().plusDays(1);
        }

        return LocalDateTime.parse(time, FORMATTER);
    }
}