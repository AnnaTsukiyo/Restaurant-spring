package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.IngredientRequestDto;

import java.util.List;

public interface IngredientService {

    void createIngredient(IngredientRequestDto ingredientRequestDto);

    void deleteIngredient(String id);

    IngredientRequestDto getIngredientById(String id);

    void updateIngredient(IngredientRequestDto ingredientRequestDto, String id);

    List<IngredientRequestDto> getAllIngredients();

}
