package com.epam.zelener.restaurant.repositories;

import com.epam.zelener.restaurant.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    @Query(value = "select m from Manager m where m.name = ?1")
    Manager findManagerByName(@Param(value = "name") String name);

    @Query(value = "select m from Manager m where m.id = :id")
    Manager findManagerById(@Param(value = "id") long id);

    @Modifying
    @Query(value = "update Manager m set m.status= 'INACTIVE' where m.name = :name")
    void updateStatus(@Param("name") String name);

    @Modifying
    @Query("update Manager m set m.name = :name where m.id = :id")
    void updateName(@Param(value = "id") String id, @Param(value = "name") String name);

}
