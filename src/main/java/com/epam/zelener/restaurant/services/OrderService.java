package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.OrderDto;
import com.epam.zelener.restaurant.model.Order;
import com.epam.zelener.restaurant.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public interface OrderService {

    @Transactional
    OrderDto findOrderById(Long orderId);

    @Transactional
    void createAnOrder(OrderDto orderDto);

    @Transactional
    Order findUserOrder(User user);

    @Modifying
    @Transactional
    void updateOrder(OrderDto orderDto, long id);

    @Modifying
    @Transactional
    void updateOrderStatus( Long id, String status);

    @Transactional
    List<OrderDto> showAllOrder();
}
