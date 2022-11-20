package com.epam.zelener.restaurant.repositories;

import com.epam.zelener.restaurant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT u FROM User u WHERE u.email = ?1")
    User findUserByEmail(String email);

    @Query(value = "SELECT u FROM User u WHERE u.phoneNumber = ?1")
    User findUserByPhoneNumber(String phoneNumber);

    @Modifying
    @Query(value = "UPDATE user SET user.status = 'INACTIVE' WHERE email =?1", nativeQuery = true)
    void updateStatus(String email);

}
