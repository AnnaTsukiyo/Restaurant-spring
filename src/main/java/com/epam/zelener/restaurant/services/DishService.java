package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.DishCreateDto;
import com.epam.zelener.restaurant.dtos.DishRequestDto;
import com.epam.zelener.restaurant.dtos.FullDishDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public interface DishService {

    @Transactional
    Optional<FullDishDto> createDish(DishCreateDto dishCreateDto);

    @Modifying
    @Transactional
    FullDishDto deactivateDish(String title);

    @Transactional
    Optional<FullDishDto> getDishByTitle(String title);

    @Modifying
    @Transactional
    FullDishDto updateDishTitle(String id, String title);

    Optional<FullDishDto> getDishById(String id);

    @Modifying
    @Transactional
    DishRequestDto updateDish(DishRequestDto dishDto, String title);

    @Transactional
    List<FullDishDto> getAllDish();

    @Transactional
    List<FullDishDto> sortingBy(String sortingOption, String page);

    @Transactional
    List<FullDishDto> getByCategory(String category, String page);

    boolean isStatusActive(String title);

}
