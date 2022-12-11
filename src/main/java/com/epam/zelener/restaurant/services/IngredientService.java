package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullIngredientDto;
import com.epam.zelener.restaurant.dtos.IngredientCreateDto;
import com.epam.zelener.restaurant.dtos.IngredientRequestDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public interface IngredientService {

    @Transactional
    Optional<FullIngredientDto> createIngredient(IngredientCreateDto ingredientRequestDto);

    @Transactional
    FullIngredientDto deactivateIngredient(String id);

    @Transactional
    Optional<FullIngredientDto> getIngredientById(String id);

    @Transactional
    IngredientRequestDto updateIngredient(IngredientRequestDto ingredientRequestDto, String id);

    @Modifying
    @Transactional
    IngredientRequestDto updateIngredientQuantity(String id, String quantity);

    @Transactional
    List<FullIngredientDto> getAllIngredients();

    boolean isStatusActive(String id);

}
