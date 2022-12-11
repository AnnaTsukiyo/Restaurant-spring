package com.epam.zelener.restaurant.controllers;

import com.epam.zelener.restaurant.dtos.DishCreateDto;
import com.epam.zelener.restaurant.dtos.FullDishDto;
import com.epam.zelener.restaurant.exceptions.DishNotFoundSuchElementException;
import com.epam.zelener.restaurant.model.Food;
import com.epam.zelener.restaurant.model.Ingredient;
import com.epam.zelener.restaurant.services.DishService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.epam.zelener.restaurant.model.Categories.DRINKS;
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
class DishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DishService dishService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private FullDishDto fullDishDto;

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @BeforeEach
    void init() {
        fullDishDto = new FullDishDto("1", "Sushi Dragon", "200", "true", "250", "SUSHI", "ACTIVE",
                List.of(new Ingredient(1L, List.of(new Food(1L, "Salmon", "Smoked salmon from Norway", true,
                        LocalDate.of(2022, Month.DECEMBER, 1),
                        LocalDate.of(2022, Month.DECEMBER, 10), LocalDateTime.now(), LocalDateTime.now())),
                        true, 20, 3)));
    }

    @DisplayName("GET dish by title -- positive scenario")
    @Test
    void getDishByTitle_positiveTest() throws Exception {

        lenient().when(dishService.getDishByTitle("Sushi Dragon")).thenReturn(Optional.ofNullable(fullDishDto));
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.writeValueAsString(fullDishDto);
        mockMvc.perform(get("/api/dish/get/{title}", "Sushi Dragon")
                        .content(mapper.writeValueAsBytes(fullDishDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", is("Sushi Dragon")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category", is("SUSHI")))
                .andDo(print());
    }

    @DisplayName("GET dish by title -- negative scenario")
    @Test
    void getDishByTitle_negativeTest() throws Exception {
        lenient().when(dishService.getDishByTitle("Sushi Dragon")).thenThrow(DishNotFoundSuchElementException.class);
        mockMvc.perform(get("/api/dish/get/{title}", fullDishDto.getTitle()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Sushi Dragon -- Dish with such title {} doesn't exist "));
        verify(dishService, times(1)).getDishByTitle("Sushi Dragon");
    }

    @DisplayName("GET by category -- positive scenario")
    @Test
    void getByCategory_positiveTest() throws Exception {
        FullDishDto greenTea = new FullDishDto("2", "Green tea", "35", "true", "250", "DRINKS", "ACTIVE",
                List.of(new Ingredient(2L, List.of(new Food(2L, "Green tea", "Green tea Ahmad",
                        true, LocalDate.of(2022, Month.DECEMBER, 1),
                        LocalDate.of(2023, Month.JANUARY, 25),
                        LocalDateTime.now(), LocalDateTime.now())),
                        true, 1, 1)));
        FullDishDto coffeeBlack = new FullDishDto("3", "Coffee Black", "40", "true", "300", String.valueOf(DRINKS), "ACTIVE", List.of(
                new Ingredient(2L, List.of(new Food(2L, "Coffee", "  Coffee Robusta 10",
                        true, LocalDate.of(2022, Month.DECEMBER, 1),
                        LocalDate.of(2023, Month.DECEMBER, 10),
                        LocalDateTime.now(), LocalDateTime.now())), true, 20, 1)));

        when(dishService.getByCategory("DRINKS", "0")).thenReturn(List.of(greenTea, coffeeBlack));
        mockMvc.perform(get("/api/dish/get/by/DRINKS/0"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", is("Green tea")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title", is("Coffee Black")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].category", is("DRINKS")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].category", is("DRINKS")));
    }

    @DisplayName("GET by category -- negative scenario")
    @Test
    void getByCategory_negativeTest() throws Exception {
        mockMvc.perform(get("/api/dish/get/by/DRINKS$/0"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("DRINKS$ -- Given category {} is not found"));
    }

    @DisplayName("GET all dishes -- positive scenario")
    @Test
    void findAllDish_positiveTest() throws Exception {
        lenient().when(dishService.getAllDish()).thenReturn(List.of(fullDishDto));
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc.perform(get("/api/dish/all")
                        .content(mapper.writeValueAsBytes(fullDishDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", is("Sushi Dragon")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].category", is("SUSHI")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", is("ACTIVE")));

        verify(dishService, times(1)).getAllDish();
    }

    @DisplayName("SORT by title -- positive scenario")
    @Test
    void sortByTitle_PositiveTest() throws Exception {
        FullDishDto mushroomSoup = new FullDishDto();
        mushroomSoup.setTitle("Mushroom Soup");
        FullDishDto cheesecake = new FullDishDto();
        cheesecake.setTitle("Cheesecake");
        when(dishService.sortingBy("title", "0")).thenReturn(Stream.of(mushroomSoup, cheesecake)
                .sorted(Comparator.comparing(FullDishDto::getTitle))
                .collect(Collectors.toList()));

        mockMvc.perform(get("/api/dish/sort/by/title/0"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", is("Cheesecake")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title", is("Mushroom Soup")));
    }

    @DisplayName("SORT by price -- positive scenario")
    @Test
    void sortByPrice_positiveTest() throws Exception {

        FullDishDto greenTea = new FullDishDto("2", "Green tea", "35", "true", "250", "DRINKS", "ACTIVE",
                List.of(new Ingredient(2L, List.of(new Food(2L, "Green tea", "Green tea Ahmad",
                        true, LocalDate.of(2022, Month.DECEMBER, 1),
                        LocalDate.of(2023, Month.JANUARY, 25),
                        LocalDateTime.now(), LocalDateTime.now())),
                        true, 1, 1)));
        FullDishDto coffeeBlack = new FullDishDto("3", "Coffee Black", "40", "true", "300", String.valueOf(DRINKS), "ACTIVE", List.of(
                new Ingredient(2L, List.of(new Food(2L, "Coffee", "  Coffee Robusta 10",
                        true, LocalDate.of(2022, Month.DECEMBER, 1),
                        LocalDate.of(2023, Month.DECEMBER, 10),
                        LocalDateTime.now(), LocalDateTime.now())), true, 20, 1)));

        when(dishService.sortingBy("price", "0")).thenReturn(List.of(greenTea, coffeeBlack));

        mockMvc.perform(get("/api/dish/sort/by/price/0"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", is("Green tea")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price", is("35")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title", is("Coffee Black")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].price", is("40")));

    }

    @DisplayName("SORT by price -- negative scenario")
    @Test
    void sortBy_NegativeTest() throws Exception {
        mockMvc.perform(get("/api/dish/sort/by/price$/0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Incorrect sorting type (must be price or title)"));
    }

    @DisplayName("DEACTIVATE dish-- positive scenario")
    @Test
    void deactivateDish_positiveTest() throws Exception {

        when(dishService.deactivateDish(fullDishDto.getTitle())).thenReturn(fullDishDto);
        when(dishService.isStatusActive(fullDishDto.getTitle())).thenReturn(true);

        mockMvc.perform(delete("/api/dish/deactivate/{title}", fullDishDto.getTitle()))
                .andExpect(status().isOk())
                .andExpect(content().string(fullDishDto.getTitle() + " –- Dish status with title {} is changed to INACTIVE"));
    }

    @DisplayName("DEACTIVATE dish -- negative scenario")
    @Test
    void deactivateDish_negativeTest() throws Exception {
        when(dishService.isStatusActive(fullDishDto.getTitle())).thenReturn(false);

        mockMvc.perform(delete("/api/dish/deactivate/{title}", fullDishDto.getTitle()))
                .andExpect(status().isConflict())
                .andExpect(content().string(fullDishDto.getTitle() + " –- Dish with such title is already INACTIVE"));
    }

    @DisplayName("CREATE dish -- positive scenario ")
    @Test
    void createDish_positiveTest() throws Exception {

        DishCreateDto createdDish = new DishCreateDto("Sushi Dragon", "200", "250", "SUSHI");
        when(dishService.createDish(createdDish)).thenReturn(Optional.of(fullDishDto));
        when(dishService.getDishByTitle("Sushi Dragon")).thenThrow(DishNotFoundSuchElementException.class);

        mockMvc.perform(post("/api/dish/create")
                        .content(toJson(createdDish))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(createdDish.getTitle() + " -- A new dish with a title {} is created"));
    }

    @DisplayName("CREATE user -- negative scenario")
    @Test
    void createDish_negativeTest() throws Exception {

        DishCreateDto createdDish = new DishCreateDto("Sushi Dragon", "200", "250", null);
        lenient().when(dishService.createDish(createdDish)).thenReturn(Optional.of(fullDishDto));
        lenient().when(dishService.getDishByTitle("Sushi Dragon")).thenReturn(Optional.of(fullDishDto));

        mockMvc.perform(post("/api/dish/create")
                        .content(toJson(createdDish))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasItem("must not be blank")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fields", hasItem("category")));

        verify(dishService, times(0)).createDish(createdDish);
    }
}

