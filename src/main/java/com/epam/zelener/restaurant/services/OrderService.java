package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullOrderDto;
import com.epam.zelener.restaurant.dtos.OrderCreateDto;
import com.epam.zelener.restaurant.dtos.OrderRequestDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public interface OrderService {

    @Transactional
    Optional<FullOrderDto> findOrderById(String orderId);

    @Transactional
    Optional<FullOrderDto> createOrder(OrderCreateDto orderDto);

    @Modifying
    @Transactional
    OrderRequestDto updateOrder(OrderRequestDto orderDto, String id);

    @Modifying
    @Transactional
    OrderRequestDto updateOrderStatus(String id, String status);

    @Transactional
    List<FullOrderDto> showAllOrder();
}
