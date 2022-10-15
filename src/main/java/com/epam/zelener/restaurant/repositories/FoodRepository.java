package com.epam.zelener.restaurant.repositories;

import com.epam.zelener.restaurant.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Integer> {
}
