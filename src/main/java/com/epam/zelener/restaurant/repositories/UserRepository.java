package com.epam.zelener.restaurant.repositories;

import com.epam.zelener.restaurant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByEmail(String email);

    User findUserByPhoneNumber(String phoneNumber);

    @Modifying
    @Query(value = "UPDATE user SET is_active = false WHERE email =?1", nativeQuery = true)
    void updateStatus(String email);

}
