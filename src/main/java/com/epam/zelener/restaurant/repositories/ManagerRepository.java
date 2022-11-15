package com.epam.zelener.restaurant.repositories;

import com.epam.zelener.restaurant.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Integer> {

    Manager findManagerByName(String name);

    Manager findManagerById(String id);

    void deleteManagerByName(String name);

}