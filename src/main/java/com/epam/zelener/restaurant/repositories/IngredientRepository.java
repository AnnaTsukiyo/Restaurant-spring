package com.epam.zelener.restaurant.repositories;

import com.epam.zelener.restaurant.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    @Query(value = "select i from Ingredient i where i.id = :id")
    Ingredient findIngredientById(@Param(value = "id") String id);

    @Modifying
    @Query(value = "update Ingredient i set i.isActive ='false' WHERE i.id = :id")
    void deactivateIngredientById(@Param(value = "id") String id);

    @Modifying
    @Query("update Ingredient i set i.quantity = :quantity WHERE i.id = :id")
    void updateQuantity(@Param(value = "id") String id);

}
