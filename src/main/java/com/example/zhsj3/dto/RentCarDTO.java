package com.example.zhsj3.dto;

import lombok.Data;

@Data
public class RentCarDTO {

    private Long userId;

    private Long carId;

    private String startTime;

    private String expectedReturnTime;

    private String pickupLocation;

    private String returnLocation;
}