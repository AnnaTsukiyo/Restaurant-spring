package com.epam.zelener.restaurant.repositories;

import com.epam.zelener.restaurant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT u FROM User u WHERE u.email = ?1")
    User findUserByEmail(@Param(value = "email") String email);

    @Query(value = "SELECT u FROM User u WHERE u.phoneNumber = ?1")
    User findUserByPhoneNumber(@Param(value = "phoneNumber") String phoneNumber);

    @Modifying
    @Query(value = "UPDATE user u SET u.status = 'INACTIVE' WHERE u.email =?1", nativeQuery = true)
    void updateStatus(@Param(value = "email") String email);

    @Modifying
    @Query(value = "UPDATE user u SET u.address = :address WHERE u.phone_number=:phoneNumber", nativeQuery = true)
    void updateAddress(@Param(value = "id") String phoneNumber, @Param(value = "address") String address);


}
