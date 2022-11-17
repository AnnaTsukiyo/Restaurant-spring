package com.epam.zelener.restaurant.repositories;

import com.epam.zelener.restaurant.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

    @Query(value = "SELECT i FROM Ingredient i WHERE i.id = ?1")
    Ingredient findIngredientById(String id);

    @Modifying
    @Query(value = "UPDATE ingredient SET is_active = false WHERE ingredient_id = ?1", nativeQuery = true)
    void deleteIngredientById(String id);

}
