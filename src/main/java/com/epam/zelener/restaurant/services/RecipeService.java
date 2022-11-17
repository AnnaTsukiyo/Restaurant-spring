package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullRecipeDto;
import com.epam.zelener.restaurant.dtos.RecipeRequestDto;

import java.util.List;

public interface RecipeService {

    void createRecipe(RecipeRequestDto recipeRequestDto);

    void deleteRecipe(String title);

    RecipeRequestDto getRecipeByTitle(String title);

    void updateRecipe(RecipeRequestDto recipeRequestDto, String title);

    List<FullRecipeDto> getAllRecipe();

}
