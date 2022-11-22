package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullRecipeDto;
import com.epam.zelener.restaurant.dtos.RecipeRequestDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public interface RecipeService {

    @Transactional
    void createRecipe(RecipeRequestDto recipeRequestDto);

    @Transactional
    void deleteRecipe(String title);

    @Transactional
    RecipeRequestDto getRecipeByTitle(String title);

    @Modifying
    @Transactional
    void updateRecipe(RecipeRequestDto recipeRequestDto, String title);

    @Transactional
    void updateRecipeTitle(Long id, String title);

    @Transactional
    List<FullRecipeDto> getAllRecipe();

}
