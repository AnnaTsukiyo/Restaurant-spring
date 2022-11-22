package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FoodRequestDto;
import com.epam.zelener.restaurant.dtos.FullFoodDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface FoodService {

    @Transactional
    void createFood(FoodRequestDto foodRequestDto);

    @Modifying
    @Transactional
    void deleteFood(String title);

    @Transactional
    FoodRequestDto getFoodByTitle(String title);

    @Modifying
    @Transactional
    void updateFood(FoodRequestDto foodRequestDto, String title);

    @Modifying
    @Transactional
    void updateFoodTitle(long id, String title);

    @Transactional
    List<FullFoodDto> getAllFood();

}

