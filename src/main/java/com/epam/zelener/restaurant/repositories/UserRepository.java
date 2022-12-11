package com.epam.zelener.restaurant.repositories;

import com.epam.zelener.restaurant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "select u from User u where u.email = ?1")
    User findUserByEmail(@Param(value = "email") String email);

    @Query(value = "select u from User u where u.phoneNumber = :phoneNumber")
    User findUserByPhoneNumber(@Param(value = "phoneNumber") String phoneNumber);

    @Modifying(clearAutomatically = true)
    @Query(value = "update User u set u.status = 'INACTIVE' where u.email = :email")
    void updateStatus(@Param(value = "email") String email);

    @Modifying(clearAutomatically = true)
    @Query(value = "update User u set u.address = :address where u.email = :email")
    void updateAddress(@Param(value = "email") String email, @Param(value = "address") String address);

}
