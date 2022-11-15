package com.epam.zelener.restaurant.repositories;

import com.epam.zelener.restaurant.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

    Ingredient findIngredientById(String id);

    void deleteIngredientById(String id);

}
