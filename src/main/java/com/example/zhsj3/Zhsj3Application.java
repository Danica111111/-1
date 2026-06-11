package com.example.zhsj3;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.zhsj3.mapper")
public class Zhsj3Application {

    public static void main(String[] args) {
        SpringApplication.run(Zhsj3Application.class, args);
    }

}