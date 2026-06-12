package com.example.zhsj3.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("rental_order")
public class RentalOrder {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long carId;

    private LocalDateTime startTime;

    private LocalDateTime expectedReturnTime;

    private LocalDateTime actualReturnTime;

    private BigDecimal deposit;

    private BigDecimal totalFee;

    private String status;

    private LocalDateTime createTime;

    private String pickupLocation;

    private String returnLocation;
}