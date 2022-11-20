package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.OrderDto;
import com.epam.zelener.restaurant.model.Order;
import com.epam.zelener.restaurant.model.User;
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

    @Transactional
    void updateOrderStatus(OrderDto orderDto, String id);

    @Transactional
    List<OrderDto> showAllOrder();
}
