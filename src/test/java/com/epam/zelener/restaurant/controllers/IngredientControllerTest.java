package com.epam.zelener.restaurant.controllers;

import com.epam.zelener.restaurant.dtos.FullIngredientDto;
import com.epam.zelener.restaurant.dtos.IngredientCreateDto;
import com.epam.zelener.restaurant.dtos.IngredientRequestDto;
import com.epam.zelener.restaurant.model.Food;
import com.epam.zelener.restaurant.services.IngredientService;
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
class IngredientControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private IngredientService ingredientService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private FullIngredientDto fullIngredientDto;

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @BeforeEach
    void init() {
        fullIngredientDto = new FullIngredientDto("1", List.of(new Food(1L, "Salmon", "Smoked salmon from Norway", true,
                LocalDate.of(2022, Month.DECEMBER, 1),
                LocalDate.of(2022, Month.DECEMBER, 10), LocalDateTime.now(), LocalDateTime.now())), "true", "20", "3", "ACTIVE");
    }

    @DisplayName("GET ingredient by id -- positive scenario")
    @Test
    void getIngredientById_positiveTest() throws Exception {

        lenient().when(ingredientService.getIngredientById("1")).thenReturn(Optional.ofNullable(fullIngredientDto));
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.writeValueAsString(fullIngredientDto);
        mockMvc.perform(get("/api/ingredient/get/{id}", "1")
                        .content(mapper.writeValueAsBytes(fullIngredientDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity", is("20")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.units", is("3")))
                .andDo(print());
    }

    @DisplayName("GET ingredient by id -- negative scenario")
    @Test
    void getIngredientById_negativeTest() throws Exception {
        lenient().when(ingredientService.getIngredientById("1")).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/api/ingredient/get/{id}", "1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("1 -- Ingredient with such id {} doesn't exist "));
        verify(ingredientService, times(1)).getIngredientById("1");
    }

    @DisplayName("GET all ingredients -- positive scenario")
    @Test
    void findAllFood_positiveTest() throws Exception {
        lenient().when(ingredientService.getAllIngredients()).thenReturn(List.of(fullIngredientDto));
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc.perform(get("/api/ingredient/all")
                        .content(mapper.writeValueAsBytes(fullIngredientDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity", is("20")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].units", is("3")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", is("ACTIVE")));

        verify(ingredientService, times(1)).getAllIngredients();
    }

    @DisplayName("CREATE ingredient -- positive scenario ")
    @Test
    void createIngredient_positiveTest() throws Exception {

        IngredientCreateDto createDto = new IngredientCreateDto("1", "20", "3", "ACTIVE");
        when(ingredientService.createIngredient(createDto)).thenReturn(Optional.of(fullIngredientDto));
        when(ingredientService.getIngredientById("1")).thenThrow(NoSuchElementException.class);

        mockMvc.perform(post("/api/ingredient/create")
                        .content(toJson(createDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(createDto.getId() + " -- A new ingredient with id {} is created"));
    }

    @DisplayName("CREATE ingredient -- negative scenario")
    @Test
    void createIngredient_negativeTest() throws Exception {

        IngredientCreateDto createDto = new IngredientCreateDto("1", "", "3", "ACTIVE");
        when(ingredientService.createIngredient(createDto)).thenReturn(Optional.of(fullIngredientDto));
        when(ingredientService.getIngredientById("1")).thenThrow(NoSuchElementException.class);

        mockMvc.perform(post("/api/ingredient/create")
                        .content(toJson(createDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasItem("Quantity must follow the pattern: X, XX, XXX, XXXX")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fields", hasItem("quantity")));

        verify(ingredientService, times(0)).createIngredient(createDto);
    }

    @DisplayName("UPDATE ingredient by id -- positive scenario")
    @Test
    void updateIngredient_positiveTest() throws Exception {

        IngredientRequestDto updatedIngredient = new IngredientRequestDto();
        updatedIngredient.setUnits("3");
        updatedIngredient.setQuantity("5");
        fullIngredientDto.setUnits("3");
        fullIngredientDto.setQuantity("5");

        when(ingredientService.updateIngredient(updatedIngredient, "1")).thenReturn(updatedIngredient);

        mockMvc.perform(patch("/api/ingredient/update/{id}", fullIngredientDto.getId())
                        .content(toJson(updatedIngredient))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(fullIngredientDto.getId() + " -- Ingredient with a given id {} is updated successfully"));
    }

    @DisplayName("DEACTIVATE ingredient-- positive scenario")
    @Test
    void deactivateManager_positiveTest() throws Exception {

        when(ingredientService.deactivateIngredient(fullIngredientDto.getId())).thenReturn(fullIngredientDto);
        when(ingredientService.isStatusActive(fullIngredientDto.getId())).thenReturn(true);

        mockMvc.perform(delete("/api/ingredient/deactivate/" + fullIngredientDto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(fullIngredientDto.getId() + " –- Ingredient with an id{} is successfully deactivated"));
    }

    @DisplayName("DEACTIVATE ingredient-- negative scenario")
    @Test
    void deactivateManager_negativeTest() throws Exception {
        when(ingredientService.isStatusActive(fullIngredientDto.getId())).thenReturn(false);
        mockMvc.perform(delete("/api/ingredient/deactivate/" + fullIngredientDto.getId()))
                .andExpect(status().isConflict())
                .andExpect(content().string(fullIngredientDto.getId() + " -– Ingredient with such id is already INACTIVE"));
    }
}
