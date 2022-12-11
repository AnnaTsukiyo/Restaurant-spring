package com.epam.zelener.restaurant.repositories;

import com.epam.zelener.restaurant.model.Order;
import com.epam.zelener.restaurant.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.status = :status")
    Order getOrderByStatus(@Param("status") OrderStatus status);

    @Query("select o from Order o where o.id = :id")
    Order findOrderById(@Param("id") String id);

//    @Query("select o from Order o where o.userId = :user")
//    Order getByUser(@Param("user") User user);

    @Modifying
    @Query("update Order o set o.status= :status where o.id= :id")
    void updateOrderStatus(@Param("id") String id, @Param("status") String status);
}
