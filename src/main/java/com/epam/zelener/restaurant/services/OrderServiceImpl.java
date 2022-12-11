package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullOrderDto;
import com.epam.zelener.restaurant.dtos.OrderCreateDto;
import com.epam.zelener.restaurant.dtos.OrderRequestDto;
import com.epam.zelener.restaurant.model.Order;
import com.epam.zelener.restaurant.repositories.DishRepository;
import com.epam.zelener.restaurant.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Resource
    private final ModelMapper mapper;

    private final OrderRepository orderRepository;

    private final DishRepository dishRepository;

    @Transactional
    @Override
    public Optional<FullOrderDto> findOrderById(String orderId) {
        log.info("findOrderById with id {}", orderId);
        try {
            return Optional.of(mapper.map(orderRepository.findOrderById(orderId), FullOrderDto.class));
        } catch (IllegalArgumentException ex) {
            throw new NoSuchElementException();
        }
    }

    @Transactional
    @Override
    public Optional<FullOrderDto> createOrder(OrderCreateDto orderDto) {
        log.info("createAnOrder ", orderDto.getOrderId());
        Order order = mapper.map(orderDto,Order.class);
        log.info("New Order is created", orderDto.getOrderId());
        return findOrderById(orderDto.getOrderId());
    }

    @Transactional
    @Override
    public OrderRequestDto updateOrder(OrderRequestDto orderDto, String id) {
        log.info("updateOrder with id {}", id);
        FullOrderDto fullOrderDto = findOrderById(id).orElseThrow();
        Optional<Order> order = orderRepository.findById(Long.valueOf(fullOrderDto.getOrderId()));

        String newId = orderDto.getOrderId() == null ? id : orderDto.getOrderId();
        String totalPrice = orderDto.getTotalPrice() == null ? fullOrderDto.getTotalPrice() : orderDto.getTotalPrice();
        String methodOfReceiving = orderDto.getMethodOfReceiving() == null ? fullOrderDto.getMethodOfReceiving() : orderDto.getMethodOfReceiving();

        order.orElseThrow().setTotalPrice(Double.valueOf(totalPrice));
        order.orElseThrow().setMethodOfReceiving(methodOfReceiving);
        order.orElseThrow().setId(Long.valueOf(newId));

        Order updatedOrder = orderRepository.save(order.orElseThrow());
            log.info("Order is updated successfully");
            return mapper.map(updatedOrder, OrderRequestDto.class);
    }

    @Transactional
    @Override
    public OrderRequestDto updateOrderStatus(String id, String status) {

        log.info("updateOrderStatus by id {}", id);
        findOrderById(id).orElseThrow();
        Order order = orderRepository.findOrderById(id);
        orderRepository.updateOrderStatus(id, status);

        Order updatedOrder = orderRepository.save(order);
        log.info("Order is updated successfully");
        return mapper.map(updatedOrder, OrderRequestDto.class);

    }

    @Transactional
    @Override
    public List<FullOrderDto> showAllOrder() {
        log.info("showAllOrder order ");
        return orderRepository.findAll().stream()
                .map(e -> mapper.map(e, FullOrderDto.class))
                .collect(Collectors.toList());
    }
}
