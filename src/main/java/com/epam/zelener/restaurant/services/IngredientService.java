package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.IngredientRequestDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface IngredientService {

    @Transactional
    void createIngredient(IngredientRequestDto ingredientRequestDto);

    @Transactional
    void deleteIngredient(String id);

    @Transactional
    IngredientRequestDto getIngredientById(String id);

    @Transactional
    void updateIngredient(IngredientRequestDto ingredientRequestDto, String id);

    @Modifying
    @Transactional
    void updateIngredientQuantity(long id, int quantity);

    @Transactional
    List<IngredientRequestDto> getAllIngredients();

}
