package com.epam.zelener.restaurant.repositories;

import com.epam.zelener.restaurant.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FoodRepository extends JpaRepository<Food, Integer> {

    @Query(value = "SELECT f FROM Food f WHERE f.title = :title")
    Food findFoodByTitle(@Param("title")String title);

    @Modifying
    @Query(value = "UPDATE food SET is_active = false WHERE title = ?1", nativeQuery = true)
    void deleteFoodByTitle(@Param("title") String title);

}
