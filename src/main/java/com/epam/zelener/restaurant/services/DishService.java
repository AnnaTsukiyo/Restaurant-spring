package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.DishRequestDto;
import com.epam.zelener.restaurant.dtos.FullDishDto;

import java.util.List;

public interface DishService {

    void createDish(DishRequestDto dishRequestDto);

    void deleteDish(String title);

    DishRequestDto getDishByTitle(String title);

    void updateDish(DishRequestDto dishRequestDto, String title);

    List<FullDishDto> getAllDish();

    List<FullDishDto> sortingBy(String sortingOption, String page);

    List<FullDishDto> getByCategory(String category, String page);

}
