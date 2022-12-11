package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullRecipeDto;
import com.epam.zelener.restaurant.dtos.RecipeCreateDto;
import com.epam.zelener.restaurant.dtos.RecipeRequestDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public interface RecipeService {

    @Transactional
    Optional<FullRecipeDto> createRecipe(RecipeCreateDto recipeCreateDto);

    @Transactional
    FullRecipeDto deactivateRecipe(String title);

    @Transactional
    Optional<FullRecipeDto> getRecipeByTitle(String title);

    @Modifying
    @Transactional
    RecipeRequestDto updateRecipe(RecipeRequestDto recipeRequestDto, String title);

    @Transactional
    FullRecipeDto updateRecipeTitle(String id, String title);

    @Transactional
    List<FullRecipeDto> getAllRecipe();

    Optional<FullRecipeDto> getRecipeById(String id);

    boolean isStatusActive(String title);

}
