package com.epam.zelener.restaurant.controllers;

import com.epam.zelener.restaurant.dtos.FullRecipeDto;
import com.epam.zelener.restaurant.dtos.RecipeCreateDto;
import com.epam.zelener.restaurant.dtos.RecipeRequestDto;
import com.epam.zelener.restaurant.model.Food;
import com.epam.zelener.restaurant.model.Ingredient;
import com.epam.zelener.restaurant.services.RecipeService;
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
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private FullRecipeDto fullRecipeDto;

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @BeforeEach
    void init() {

        fullRecipeDto = new FullRecipeDto("1", "12","true", "Cooking Sushi California", "30","ACTIVE",
                List.of(new Ingredient(1L, List.of(new Food(1L, "Salmon", "Smoked salmon from Norway",
                        true, LocalDate.of(2022, Month.DECEMBER, 1),
                        LocalDate.of(2022, Month.DECEMBER, 10),
                        LocalDateTime.now(), LocalDateTime.now())), true, 20, 3)));
    }

    @DisplayName("GET all recipes -- positive scenario")
    @Test
    void findAllRecipe_positiveTest() throws Exception {
        lenient().when(recipeService.getAllRecipe()).thenReturn(List.of(fullRecipeDto));
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc.perform(get("/api/recipe/all")
                        .content(mapper.writeValueAsBytes(fullRecipeDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", is("Cooking Sushi California")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].number", is("12")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", is("ACTIVE")));

        verify(recipeService, times(1)).getAllRecipe();
    }

    @DisplayName("GET recipe by title -- positive scenario")
    @Test
    void getRecipeByTitle_positiveTest() throws Exception {

        lenient().when(recipeService.getRecipeByTitle("Cooking Sushi California")).thenReturn(Optional.ofNullable(fullRecipeDto));
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.writeValueAsString(fullRecipeDto);
        mockMvc.perform(get("/api/recipe/get/{title}", "Cooking Sushi California")
                        .content(mapper.writeValueAsBytes(fullRecipeDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", is("Cooking Sushi California")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration", is("30")))
                .andDo(print());
    }
    @DisplayName("GET dish by title -- negative scenario")
    @Test
    void getDishByTitle_negativeTest() throws Exception {
        lenient().when(recipeService.getRecipeByTitle("Cooking Sushi California")).thenThrow(NoSuchElementException.class);
        mockMvc.perform(get("/api/recipe/get/{title}", fullRecipeDto.getTitle()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cooking Sushi California -- Recipe with such email {} doesn't exist "));
        verify(recipeService, times(1)).getRecipeByTitle("Cooking Sushi California");
    }

    @DisplayName("UPDATE recipe by title -- positive scenario")
    @Test
    void updateRecipe_positiveTest() throws Exception {
        RecipeRequestDto updatedRecipe = new RecipeRequestDto();
        updatedRecipe.setDuration("150");
        updatedRecipe.setNumber("210");
        fullRecipeDto.setDuration("150");
        fullRecipeDto.setNumber("210");

        when(recipeService.updateRecipe(updatedRecipe,"Cooking Sushi California")).thenReturn(updatedRecipe);

        mockMvc.perform(patch("/api/recipe/update/{title}", "Cooking Sushi California")
                        .content(toJson(updatedRecipe))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Cooking Sushi California -- Recipe with a given title {} is updated successfully"));
    }
    @DisplayName("CREATE recipe -- positive scenario ")
    @Test
    void createRecipe_positiveTest() throws Exception {

        RecipeCreateDto recipeCreateDto = new RecipeCreateDto("1", "111","Cooking Sushi California", "60");
        when(recipeService.createRecipe(recipeCreateDto)).thenReturn(Optional.of(fullRecipeDto));
        when(recipeService.getRecipeByTitle("Cooking Sushi California")).thenThrow(NoSuchElementException.class);

        mockMvc.perform(post("/api/recipe/create")
                        .content(toJson(recipeCreateDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(recipeCreateDto.getTitle() + " -- A new recipe with a title {} is created"));
    }
    @DisplayName("CREATE recipe -- negative scenario")
    @Test
    void createRecipe_negativeTest() throws Exception {

        RecipeCreateDto recipeCreateDto = new RecipeCreateDto("1", "111","Cooking Sushi California", null);
        lenient().when(recipeService.createRecipe(recipeCreateDto)).thenReturn(Optional.of(fullRecipeDto));
        lenient().when(recipeService.getRecipeByTitle("Cooking Sushi California")).thenReturn(Optional.of(fullRecipeDto));

        mockMvc.perform(post("/api/recipe/create")
                        .content(toJson(recipeCreateDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasItem("must not be blank")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fields", hasItem("duration")));

        verify(recipeService, times(0)).createRecipe(recipeCreateDto);
    }

    @DisplayName("DEACTIVATE recipe-- positive scenario")
    @Test
    void deactivateRecipe_positiveTest() throws Exception {

        when(recipeService.deactivateRecipe(fullRecipeDto.getTitle())).thenReturn(fullRecipeDto);
        when(recipeService.isStatusActive(fullRecipeDto.getTitle())).thenReturn(true);

        mockMvc.perform(delete("/api/recipe/deactivate/{title}", fullRecipeDto.getTitle()))
                .andExpect(status().isOk())
                .andExpect(content().string(fullRecipeDto.getTitle() + " – Recipe with a title{} is successfully deactivated"));
    }

    @DisplayName("DEACTIVATE recipe -- negative scenario")
    @Test
    void deactivateRecipe_negativeTest() throws Exception {
        when(recipeService.isStatusActive(fullRecipeDto.getTitle())).thenReturn(false);
        mockMvc.perform(delete("/api/recipe/deactivate/{title}", fullRecipeDto.getTitle()))
                .andExpect(status().isConflict())
                .andExpect(content().string(fullRecipeDto.getTitle() + " –- Recipe with such title is already INACTIVE"));
    }

}
