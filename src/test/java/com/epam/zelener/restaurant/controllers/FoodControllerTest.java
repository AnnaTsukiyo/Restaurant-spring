package com.epam.zelener.restaurant.controllers;

import com.epam.zelener.restaurant.dtos.FoodCreateDto;
import com.epam.zelener.restaurant.dtos.FoodRequestDto;
import com.epam.zelener.restaurant.dtos.FullFoodDto;
import com.epam.zelener.restaurant.services.FoodService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static groovy.json.JsonOutput.toJson;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FoodControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private FoodService foodService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private FullFoodDto fullFoodDto;

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @BeforeEach
    void init() {
        fullFoodDto = new FullFoodDto("1", "Coffee", "Coffee black Arabica 5+", "ACTIVE", "true", "2022-11-10", "2023-12-29");
    }

    @DisplayName("GET food by title -- positive scenario")
    @Test
    void getFoodByTitle_positiveTest() throws Exception {

        lenient().when(foodService.getFoodByTitle("Coffee")).thenReturn(Optional.ofNullable(fullFoodDto));
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.writeValueAsString(fullFoodDto);
        mockMvc.perform(get("/api/food/get/{title}", "Coffee")
                        .content(mapper.writeValueAsBytes(fullFoodDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", is("Coffee")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("Coffee black Arabica 5+")))
                .andDo(print());
    }

    @DisplayName("GET food by title -- negative scenario")
    @Test
    void getFoodByTitle_negativeTest() throws Exception {
        lenient().when(foodService.getFoodByTitle("Coffee")).thenThrow(NoSuchElementException.class);
        mockMvc.perform(get("/api/food/get/{title}", fullFoodDto.getTitle()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Coffee -- Food with such title {} doesn't exist!"));
        verify(foodService, times(1)).getFoodByTitle("Coffee");
    }

    @DisplayName("GET all foods -- positive scenario")
    @Test
    void findAllFood_positiveTest() throws Exception {
        lenient().when(foodService.getAllFood()).thenReturn(List.of(fullFoodDto));
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc.perform(get("/api/food/all")
                        .content(mapper.writeValueAsBytes(fullFoodDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", is("Coffee")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", is("Coffee black Arabica 5+")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", is("ACTIVE")));

        verify(foodService, times(1)).getAllFood();
    }

    @DisplayName("CREATE food -- positive scenario ")
    @Test
    void createFood_positiveTest() throws Exception {

        FoodCreateDto createdFood = new FoodCreateDto("Coffee", "Coffee black Arabica 5+", "2022-11-10", "2022-12-29");
        when(foodService.createFood(createdFood)).thenReturn(Optional.of(fullFoodDto));
        when(foodService.getFoodByTitle("Coffee")).thenThrow(NoSuchElementException.class);

        mockMvc.perform(post("/api/food/create")
                        .content(toJson(createdFood))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(createdFood.getTitle() + " -- A new food with a title {} is created"));
    }

    @DisplayName("CREATE food -- negative scenario")
    @Test
    void createFood_negativeTest() throws Exception {

        FoodCreateDto createdFood = new FoodCreateDto("Coffee", "Coffee black Arabica 5+", "2022-11-10", null);
        lenient().when(foodService.createFood(createdFood)).thenReturn(Optional.of(fullFoodDto));
        lenient().when(foodService.getFoodByTitle("Coffee")).thenReturn(Optional.of(fullFoodDto));

        mockMvc.perform(post("/api/food/create")
                        .content(toJson(createdFood))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasItem("must not be blank")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fields", hasItem("expDate")));

        verify(foodService, times(0)).createFood(createdFood);
    }

    @DisplayName("UPDATE food by title -- positive scenario")
    @Test
    void updateFood_positiveTest() throws Exception {
        FoodRequestDto updatedDish = new FoodRequestDto();
        updatedDish.setDescription("Coffee black Robusta");
        fullFoodDto.setDescription("Coffee black Robusta");

        when(foodService.updateFood(updatedDish, "Coffee")).thenReturn(updatedDish);

        mockMvc.perform(patch("/api/food/{title}", fullFoodDto.getTitle())
                        .content(toJson(updatedDish))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Coffee -- Food with a given title {} is updated successfully"));
    }

    @DisplayName("DEACTIVATE user-- positive scenario")
    @Test
    void deactivateUser_positiveTest() throws Exception {
        when(foodService.deactivateFood(fullFoodDto.getTitle())).thenReturn(fullFoodDto);
        when(foodService.isStatusActive(fullFoodDto.getTitle())).thenReturn(true);

        mockMvc.perform(delete("/api/food/deactivate/{title}", fullFoodDto.getTitle()))
                .andExpect(status().isOk())
                .andExpect(content().string(fullFoodDto.getTitle() + " –- Food status with title {} is changed to INACTIVE"));
    }

    @DisplayName("DEACTIVATE user-- negative scenario")
    @Test
    void deactivateUser_negativeTest() throws Exception {
        when(foodService.isStatusActive(fullFoodDto.getTitle())).thenReturn(false);

        mockMvc.perform(delete("/api/food/deactivate/{title}", fullFoodDto.getTitle()))
                .andExpect(status().isConflict())
                .andExpect(content().string(fullFoodDto.getTitle() + " – Food with such title is already INACTIVE"));
    }
}
