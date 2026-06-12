package com.example.zhsj3.dto;

import lombok.Data;

@Data
public class ReserveCarDTO {

    private Long userId;

    private Long carId;

    private String reserveTime;

    private String pickupLocation;

    private String returnLocation;
}