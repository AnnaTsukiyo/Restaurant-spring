package com.epam.zelener.restaurant.repositories;

import com.epam.zelener.restaurant.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    @Modifying
    @Query(value = "DELETE FROM Recipe r WHERE r.title=:title")
    void deleteRecipeByTitle(@Param("title") String title);

    @Query(value = "SELECT r FROM Recipe r WHERE r.title LIKE %:title%")
    Recipe findRecipeByTitle(@Param("title") String title);

    @Modifying
    @Query("UPDATE Recipe r set r.title = :title where r.id = ?1")
    void updateTitle(@Param(value = "id") Long id, @Param(value = "title") String title);
}
