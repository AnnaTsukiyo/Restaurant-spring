package com.epam.zelener.restaurant.repositories;

import com.epam.zelener.restaurant.model.Categories;
import com.epam.zelener.restaurant.model.Dish;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Integer> {

    @Modifying
    @Query(value = "SELECT d FROM Dish d WHERE d.title LIKE %:title%")
    Dish findDishByTitle(@Param("title") String title);

    @Query(value = "SELECT d FROM Dish d WHERE d.category = :category")
    List<Dish> findByCategory(Pageable pageable, @Param("category") Categories category);

    @Modifying
    @Query(value = "UPDATE dish SET is_active = false WHERE title = ?1", nativeQuery = true)
    void deleteDishByTitle(@Param("title") String title);

}
