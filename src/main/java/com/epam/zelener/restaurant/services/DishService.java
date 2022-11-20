package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.DishRequestDto;
import com.epam.zelener.restaurant.dtos.FullDishDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface DishService {

    @Transactional
    void createDish(DishRequestDto dishRequestDto);

    @Transactional
    void deleteDish(String title);

    @Transactional
    DishRequestDto getDishByTitle(String title);

    @Modifying
    @Transactional
    void updateDish(DishRequestDto dishRequestDto, String title);

    @Transactional
    List<FullDishDto> getAllDish();

    @Transactional
    List<FullDishDto> sortingBy(String sortingOption, String page);

    @Transactional
    List<FullDishDto> getByCategory(String category, String page);

}
