package com.epam.zelener.restaurant.repositories;

import com.epam.zelener.restaurant.model.Order;
import com.epam.zelener.restaurant.model.OrderStatus;
import com.epam.zelener.restaurant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.status =?1")
    Order getOrderByStatus(@Param("status") OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.id =?1")
    Order getOrderById(@Param("id") Long id);

    @Query("SELECT o from Order o where o.userId = :user")
    Order getByUser(@Param("user") User user);
}
