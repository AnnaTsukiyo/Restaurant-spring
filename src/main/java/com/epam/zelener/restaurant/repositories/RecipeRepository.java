package com.epam.zelener.restaurant.repositories;

import com.epam.zelener.restaurant.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    void deleteRecipeByTitle(String title);

    Recipe findRecipeByTitle(String title);
}
