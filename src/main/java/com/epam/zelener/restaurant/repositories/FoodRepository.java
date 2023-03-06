package com.epam.zelener.restaurant.repositories;

import com.epam.zelener.restaurant.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    @Query(value = "select f from Food f where f.id = :id")
    Food findFoodById(@Param("id") String id);

    @Query(value = "select f from Food f where f.title = :title")
    Food findFoodByTitle(@Param("title") String title);

    @Modifying
    @Query("update Food f set f.title = :title where f.id = :id")
    void updateTitle(@Param(value = "id") String id, @Param(value = "title") String title);

    @Modifying
    @Query(value = "update Food f set f.status = 'INACTIVE' where f.title = :title")
    void updateStatus(@Param(value = "title") String title);

}
