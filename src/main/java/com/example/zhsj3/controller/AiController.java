package com.example.zhsj3.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.zhsj3.common.Result;
import com.example.zhsj3.dto.AiRecommendDTO;
import com.example.zhsj3.entity.Car;
import com.example.zhsj3.mapper.CarMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin
public class AiController {

    @Autowired
    private CarMapper carMapper;

    @Value("${moonshot.api.key:}")
    private String apiKey;

    @Value("${moonshot.api.url:https://api.moonshot.cn/v1/chat/completions}")
    private String apiUrl;

    @Value("${moonshot.api.model:moonshot-v1-8k}")
    private String model;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * AI 智能选车助手
     */
    @PostMapping("/recommend")
    public Result<String> recommendCar(@RequestBody AiRecommendDTO aiRecommendDTO) {
        try {
            if (apiKey == null || apiKey.trim().isEmpty()) {
                return Result.error("未配置大模型 API Key，请在 application.properties 中配置 moonshot.api.key");
            }

            QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", "AVAILABLE");

            List<Car> availableCars = carMapper.selectList(queryWrapper);

            if (availableCars == null || availableCars.isEmpty()) {
                return Result.error("当前没有可用车辆");
            }

            String carListText = availableCars.stream()
                    .map(car ->
                            "车辆ID：" + car.getId() +
                                    "，品牌：" + car.getBrand() +
                                    "，车型：" + car.getModel() +
                                    "，类型：" + car.getCarType() +
                                    "，座位数：" + car.getSeats() +
                                    "，日租金：" + car.getDailyPrice() + "元/天" +
                                    "，描述：" + car.getDescription()
                    )
                    .collect(Collectors.joining("\n"));

            String prompt =
                    "你是汽车租赁系统的 AI 智能选车助手。\n" +
                            "你的任务是根据用户自然语言租车需求，从可用车辆列表中推荐最合适的一辆车。\n\n" +
                            "用户需求：\n" +
                            aiRecommendDTO.getRequirement() + "\n\n" +
                            "当前可用车辆列表：\n" +
                            carListText + "\n\n" +
                            "请你按照以下格式回答：\n" +
                            "推荐车辆：品牌 + 车型\n" +
                            "推荐理由：\n" +
                            "1. 从座位数角度说明为什么合适\n" +
                            "2. 从预算角度说明为什么合适\n" +
                            "3. 从出行场景角度说明为什么合适\n" +
                            "4. 如果没有完全符合需求的车辆，请推荐最接近的一辆，并说明原因\n\n" +
                            "注意：只能从上面的可用车辆列表中选择，不能编造车辆。";

            String aiResult = callKimiApi(prompt);

            return Result.success(aiResult);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("AI 推荐失败：" + e.getMessage());
        }
    }

    /**
     * 调用 Kimi / Moonshot 大模型 API
     */
    private String callKimiApi(String prompt) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of(
                                "role", "system",
                                "content", "你是一个专业的汽车租赁推荐助手，回答要简洁、清楚、符合用户租车需求。"
                        ),
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                ),
                "temperature", 0.3,
                "max_tokens", 800
        );

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new RuntimeException("大模型接口调用失败，状态码：" + response.statusCode() + "，返回：" + response.body());
        }

        JsonNode root = objectMapper.readTree(response.body());

        return root
                .path("choices")
                .get(0)
                .path("message")
                .path("content")
                .asText();
    }
}