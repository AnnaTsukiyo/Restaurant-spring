package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullDishDto;
import com.epam.zelener.restaurant.dtos.FullOrderDto;
import com.epam.zelener.restaurant.dtos.OrderCreateDto;
import com.epam.zelener.restaurant.dtos.OrderRequestDto;
import com.epam.zelener.restaurant.model.*;
import com.epam.zelener.restaurant.repositories.DishRepository;
import com.epam.zelener.restaurant.repositories.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.epam.zelener.restaurant.model.Categories.SUSHI;
import static com.epam.zelener.restaurant.model.Status.ACTIVE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private final ModelMapper mapper = new ModelMapper();
    private OrderServiceImpl orderService;
    private DishRepository dishRepository;
    private Order order;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(mapper, orderRepository, dishRepository);
        order = new Order(1L, new User(1L, "Jonas Tidermann", "+3806567898", "address", "admin@gmail.com", "passwordP1", "passwordP1",
                "2000-01-01", Role.GUEST, ACTIVE, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now()),
                List.of(new Dish(1L, 120, "Sushi California", 200, SUSHI, true, ACTIVE, List.of(
                        new Ingredient(1L, List.of(new Food(1L, "Salmon", "Smoked salmon from Norway",
                                true, ACTIVE, LocalDate.of(2022, Month.DECEMBER, 1),
                                LocalDate.of(2022, Month.DECEMBER, 10),
                                LocalDateTime.now(), LocalDateTime.now())), true, 20, 3)))), 330.30, "delivery Rocket",
                OrderStatus.NEW, LocalDateTime.now(), LocalDateTime.now());
    }

    @DisplayName("JUNIT Test OrderServiceImpl findOrderById() --positive test case scenario")
    @Test
    void findOrderById_positiveTest() {
        Order order = new Order(2L, new User(1L, "Mariam Minelli", "+3807867898", "address", "mariam@gmail.com", "MariamPass@123", "MariamPass@123",
                "2004-01-01", Role.GUEST, ACTIVE, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now()),
                List.of(new Dish(1L, 120, "Sushi California", 200, SUSHI, true, Status.ACTIVE, List.of(
                        new Ingredient(1L, List.of(new Food(1L, "Salmon", "Smoked salmon from Norway",
                                true, Status.ACTIVE, LocalDate.of(2022, Month.DECEMBER, 1),
                                LocalDate.of(2022, Month.DECEMBER, 10),
                                LocalDateTime.now(), LocalDateTime.now())), true, 20, 3)))), 330.30, "delivery Rocket",
                OrderStatus.NEW, LocalDateTime.now(), LocalDateTime.now());

        FullDishDto dto = new FullDishDto();
        dto.setId("2");
        System.out.println(order.getId());
        lenient().when(orderRepository.findOrderById("2")).thenReturn(order);

        Assertions.assertEquals(2L, order.getId());
        System.out.println("Order is found :" + orderService.findOrderById(String.valueOf(2L)));

    }

    @DisplayName("JUNIT Test OrderServiceImpl findOrderById() method --negative test case scenario")
    @Test
    void findOrderById_negativeTest() {

        lenient().when(orderRepository.findOrderById(String.valueOf(order.getId()))).thenThrow(NoSuchElementException.class);
        Assertions.assertThrows(NoSuchElementException.class, () -> orderService.findOrderById(String.valueOf(order.getId())).orElseThrow(NoSuchElementException::new));
        System.out.println("Order is not found with id{} :" + "1");
    }

    @Test
    void createOrder_positiveTest() {

        OrderCreateDto orderDto = new OrderCreateDto("1", List.of(new Dish(1L, 120, "Sushi California", 200, SUSHI, true, Status.ACTIVE,
                List.of(new Ingredient(1L, List.of(new Food(1L, "Salmon", "Smoked salmon from Norway",
                        true, Status.ACTIVE, LocalDate.of(2022, Month.DECEMBER, 1),
                        LocalDate.of(2022, Month.DECEMBER, 10),
                        LocalDateTime.now(), LocalDateTime.now())), true, 20, 3)))),
                "340", "in restaurant");
        Order order1 = mapper.map(orderDto, Order.class);

        lenient().when(orderRepository.save(order1)).thenReturn(order1);
        lenient().when(orderRepository.findOrderById("1")).thenReturn(order1);

        Optional<FullOrderDto> createdOrder = orderService.createOrder(orderDto);
        assertThat(createdOrder.orElseThrow().getOrderId()).isEqualTo("1");
    }


    @Test
    void updateOrder() {
        when(orderRepository.findOrderById("1")).thenReturn(order);
        when(orderRepository.findById((order.getId()))).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        OrderRequestDto orderRequestDto = mapper.map(order, OrderRequestDto.class);
        orderRequestDto.setMethodOfReceiving("delivery");
        orderRequestDto.setTotalPrice("210");
        OrderRequestDto requestDto = orderService.updateOrder(orderRequestDto, String.valueOf(order.getId()));

        assertThat(requestDto.getMethodOfReceiving()).isEqualTo("delivery");
        assertThat(requestDto.getTotalPrice()).isEqualTo("210.0");

    }

    @DisplayName("JUNIT Test OrderServiceImpl updateOrderStatus() --positive test case scenario")
    @Test
    void updateOrderStatus_positiveTest() {
        lenient().when(orderRepository.findOrderById(String.valueOf((order.getId())))).thenReturn(order);
        lenient().when(orderRepository.save(order)).thenReturn(order);

        FullOrderDto orderDto = new FullOrderDto();
        orderDto.setOrderId(String.valueOf(order.getId()));
        orderDto.setStatus(OrderStatus.PROCESSING);
        orderService.updateOrderStatus(orderDto.getOrderId(), String.valueOf(orderDto.getStatus()));
        assertThat(orderDto.getStatus()).isEqualTo(OrderStatus.PROCESSING);
    }

    @DisplayName("JUNIT Test OrderServiceImpl showAllOrder() --positive test case scenario")
    @Test
    void showAllOrder_positiveTest() {
        lenient().when(orderRepository.findAll()).thenReturn(List.of(order));
        List<FullOrderDto> list = orderService.showAllOrder();
        Assertions.assertFalse(list.isEmpty());
        assertThat(list.get(0).getMethodOfReceiving()).isEqualTo(order.getMethodOfReceiving());
    }
}
