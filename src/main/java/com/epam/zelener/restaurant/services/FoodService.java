package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FoodRequestDto;
import com.epam.zelener.restaurant.dtos.FullFoodDto;

import java.util.List;

public interface FoodService {

    void createFood(FoodRequestDto foodRequestDto);

    void deleteFood(String title);

    FoodRequestDto getFoodByTitle(String title);

    void updateFood(FoodRequestDto foodRequestDto, String title);

    List<FullFoodDto> getAllFood();

}

