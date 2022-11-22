package com.epam.zelener.restaurant.repositories;

import com.epam.zelener.restaurant.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {

    @Query(value = "SELECT m FROM Manager m WHERE m.name = ?1")
    Manager findManagerByName(@Param("name") String name);

    @Query(value = "SELECT m FROM Manager m WHERE m.id = ?1")
    Manager findManagerById(String id);

    @Modifying
    @Query(value = "UPDATE Manager m SET rms.manager.status= 'INACTIVE' WHERE name =?1", nativeQuery = true)
    void updateStatus(@Param("name") String name);

    @Modifying
    @Query("UPDATE Manager m SET m.name =:name WHERE m.id = ?1")
    void updateName(@Param(value = "id") long id, @Param(value = "name")  String name);

}