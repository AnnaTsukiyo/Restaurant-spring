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
public interface DishRepository extends JpaRepository<Dish, Long> {

    @Query(value = "select d from Dish d where d.id = :id")
    Dish findDishById(@Param("id") String id);

    @Query(value = "select d from Dish d where d.title like %:title%")
    Dish findDishByTitle(@Param("title") String title);

    @Query(value = "select d from Dish d where d.category = :category")
    List<Dish> findByCategory(Pageable pageable, @Param("category") Categories category);

    @Modifying
    @Query("update Dish d set d.title = :title where d.id = :id")
    void updateTitle(@Param(value = "id") String id, @Param(value = "title") String title);

    @Modifying
    @Query(value = "update Dish d SET d.isActive = 'false' where d.title = :title")
    void deactivateDishByTitle(@Param("title") String title);

}
