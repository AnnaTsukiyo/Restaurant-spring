package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.OrderDto;
import com.epam.zelener.restaurant.model.Order;
import com.epam.zelener.restaurant.model.User;
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
    public OrderDto findOrderById(Long orderId) {
        log.info("findOrderById with id {}", orderId);
        try {
            return mapper.map(orderRepository.getOrderById(orderId), OrderDto.class);
        } catch (IllegalArgumentException ex) {
            throw new NoSuchElementException();
        }
    }

    @Transactional
    @Override
    public void createAnOrder(OrderDto orderDto) {
        log.info("createAnOrder with userId {}", orderDto.getUserId());
        orderRepository.save(mapper.map(orderDto, Order.class));
    }


    @Transactional
    @Override
    public Order findUserOrder(User user) {
        log.info("findUserOrder for user {}", user);
        return orderRepository.getByUser(user);
    }

    @Transactional
    @Override
    public void updateOrder(OrderDto orderDto, long id) {
        Order order = mapper.map(orderDto, Order.class);
        log.info("updateOrder method");
        orderRepository.save(order);
    }

    @Transactional
    @Override
    public void updateOrderStatus(Long id, String status) {
        log.info("updateOrderStatus by id {}", id);
        orderRepository.updateOrderStatus(id, status);
    }

    @Transactional
    @Override
    public List<OrderDto> showAllOrder() {
        log.info("showAllOrder order ");
        return orderRepository.findAll().stream()
                .map(e -> mapper.map(e, OrderDto.class))
                .collect(Collectors.toList());
    }
}
