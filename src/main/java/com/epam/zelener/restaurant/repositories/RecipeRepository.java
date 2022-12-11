package com.epam.zelener.restaurant.repositories;

import com.epam.zelener.restaurant.model.Dish;
import com.epam.zelener.restaurant.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Modifying
    @Query(value = "delete from Recipe r where r.title=:title")
    void deactivateRecipeByTitle(@Param("title") String title);

    @Query(value = "select r from Recipe r where r.id = :id")
    Dish findRecipeById(@Param("id") String id);

    @Query(value = "select r from Recipe r where r.title like %:title%")
    Recipe findRecipeByTitle(@Param("title") String title);

    @Modifying
    @Query("update Recipe r set r.title = :title where r.id = :id")
    void updateTitle(@Param(value = "id") String id, @Param(value = "title") String title);
}
