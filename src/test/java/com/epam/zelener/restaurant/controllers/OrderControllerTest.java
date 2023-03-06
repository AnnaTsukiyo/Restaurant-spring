//package com.epam.zelener.restaurant.controllers;
//
//import com.epam.zelener.restaurant.dtos.FullOrderDto;
//import com.epam.zelener.restaurant.dtos.OrderRequestDto;
//import com.epam.zelener.restaurant.model.*;
//import com.epam.zelener.restaurant.services.OrderService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.Month;
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//import static com.epam.zelener.restaurant.model.Categories.SUSHI;
//import static groovy.json.JsonOutput.toJson;
//import static org.hamcrest.Matchers.is;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//class OrderControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private OrderService orderService;
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    private FullOrderDto fullOrderDto;
//
//    private static final ObjectMapper mapper = new ObjectMapper();
//
//    @BeforeEach
//    void setup() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//    }
//
//    @BeforeEach
//    void init() {
//
//        fullOrderDto = new FullOrderDto("1", "2", List.of(new Dish(1L, 120, "Sushi California", 200, SUSHI, true, List.of(
//                new Ingredient(1L, List.of(new Food(1L, "Salmon", "Smoked salmon from Norway",true, LocalDate.of(2022, Month.DECEMBER, 1),
//                        LocalDate.of(2022, Month.DECEMBER, 10),
//                        LocalDateTime.now(), LocalDateTime.now())), true, 20, 3)))), "330.30", "delivery Rocket", OrderStatus.NEW);
//    }
//
//    @DisplayName("GET order by id -- positive scenario")
//    @Test
//    void getDishByTitle_positiveTest() throws Exception {
//
//        lenient().when(orderService.findOrderById("1")).thenReturn(Optional.of(fullOrderDto));
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        mapper.writeValueAsString(fullOrderDto);
//        mockMvc.perform(get("/api/order/get/{id}", "1")
//                        .content(mapper.writeValueAsBytes(fullOrderDto))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice", is("330.30")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.methodOfReceiving", is("delivery Rocket")))
//                .andDo(print());
//    }
//
//    @DisplayName("GET order by id -- negative scenario")
//    @Test
//    void getDishByTitle_negativeTest() throws Exception {
//        lenient().when(orderService.findOrderById("1")).thenThrow(NoSuchElementException.class);
//        mockMvc.perform(get("/api/order/get/{id}", fullOrderDto.getOrderId()))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("1 -- Order with such id {} doesn't exist "));
//        verify(orderService, times(1)).findOrderById("1");
//    }
//
//    @DisplayName("GET all orders -- positive scenario")
//    @Test
//    void findAllOrder_positiveTest() throws Exception {
//        lenient().when(orderService.showAllOrder()).thenReturn(List.of(fullOrderDto));
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//        mockMvc.perform(get("/api/order/all")
//                        .content(mapper.writeValueAsBytes(fullOrderDto))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", is("NEW")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].totalPrice", is("330.30")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].methodOfReceiving", is("delivery Rocket")));
//
//        verify(orderService, times(1)).showAllOrder();
//    }
//
//
//    @DisplayName("UPDATE order by id -- positive scenario")
//    @Test
//    void updateOrder_positiveTest() throws Exception {
//        OrderRequestDto updatedOrder = new OrderRequestDto();
//        updatedOrder.setTotalPrice("270");
//        updatedOrder.setMethodOfReceiving("home address delivery");
//        fullOrderDto.setTotalPrice("270");
//        fullOrderDto.setMethodOfReceiving("home address delivery");
//
//        when(orderService.updateOrder(updatedOrder, "1")).thenReturn(updatedOrder);
//        mockMvc.perform(patch("/api/order/update/{id}", "1")
//                        .content(toJson(updatedOrder))
//                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string(updatedOrder.getOrderId() + " -- Order with a given id {} is updated successfully"));
//    }
//}
