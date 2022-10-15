package com.epam.zelener.restaurant.controllers;

import com.epam.zelener.restaurant.dtos.FoodRequestDto;
import com.epam.zelener.restaurant.services.FoodService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/food")
public class FoodController {
    @Resource
    private FoodService foodService;

    @PostMapping
    public void createFood(@RequestBody FoodRequestDto foodRequestDto) {
        foodService.createFood(foodRequestDto);
    }
}
