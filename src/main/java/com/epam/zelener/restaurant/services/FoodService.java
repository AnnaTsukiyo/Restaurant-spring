package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FoodCreateDto;
import com.epam.zelener.restaurant.dtos.FoodRequestDto;
import com.epam.zelener.restaurant.dtos.FullFoodDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public interface FoodService {

    @Transactional
    Optional<FullFoodDto> createFood(FoodCreateDto foodRequestDto);

    @Modifying
    @Transactional
    FullFoodDto deactivateFood(String title);

    @Transactional
    Optional<FullFoodDto> getFoodByTitle(String title);

    @Modifying
    @Transactional
    FoodRequestDto updateFood(FoodRequestDto foodRequestDto, String title);

    @Modifying
    @Transactional
    FullFoodDto updateFoodTitle(String id, String title);

    @Transactional
    List<FullFoodDto> getAllFood();

    Optional<FullFoodDto> getFoodById(String id);

    boolean isStatusActive(String title);
}

