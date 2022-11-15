package com.epam.zelener.restaurant.repositories;

import com.epam.zelener.restaurant.model.Categories;
import com.epam.zelener.restaurant.model.Dish;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Integer> {

    @Modifying
    @Query(value = "select d from Dish d WHERE d.title LIKE %:title%")
    Dish findDishByTitle(String title);

    @Query(value = "SELECT d FROM Dish d WHERE d.category = :category")
    List<Dish> findByCategory(Pageable pageable, @Param("category") Categories category);

}
