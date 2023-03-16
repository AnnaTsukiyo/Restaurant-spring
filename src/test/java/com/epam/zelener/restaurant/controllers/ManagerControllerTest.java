package com.epam.zelener.restaurant.controllers;

import com.epam.zelener.restaurant.dtos.FullManagerDto;
import com.epam.zelener.restaurant.dtos.ManagerCreateDto;
import com.epam.zelener.restaurant.dtos.ManagerRequestDto;
import com.epam.zelener.restaurant.model.Role;
import com.epam.zelener.restaurant.model.User;
import com.epam.zelener.restaurant.services.ManagerService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.epam.zelener.restaurant.model.Status.ACTIVE;
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
class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManagerService managerService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private FullManagerDto fullManagerDto;

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @BeforeEach
    void init() {
        fullManagerDto = new FullManagerDto("1L", "Scarlett", "25", List.of(new User(1L, "Jonas Tidermann", "+3806567898", "address", "admin@gmail.com", "passwordP1","passwordP1",
                "2000-01-01", Role.GUEST, ACTIVE, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now())), String.valueOf(Role.MANAGER), "ACTIVE", "Custom Manager", "10000");
    }

    @DisplayName("GET manager by id -- positive scenario")
    @Test
    void getManagerById_positiveTest() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.writeValueAsString(fullManagerDto);
        lenient().when(managerService.getManagerById("1")).thenReturn(Optional.ofNullable(fullManagerDto));
        mockMvc.perform(get("/api/manager/1")
                        .content(mapper.writeValueAsBytes(fullManagerDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role", is("MANAGER")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Scarlett")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.position", is("Custom Manager")))
                .andDo(print());
    }

    @DisplayName("GET manager by id -- negative scenario")
    @Test
    void getManagerById_negativeTest() throws Exception {
        lenient().when(managerService.getManagerById("1")).thenThrow(NoSuchElementException.class);
        mockMvc.perform(get("/api/manager/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("1 -- Manager with such id {} doesn't exist "));
        verify(managerService, times(1)).getManagerById("1");
    }

    @DisplayName("GET all managers -- positive scenario")
    @Test
    void findAllManagers_positiveTest() throws Exception {
        lenient().when(managerService.getAllManager()).thenReturn(List.of(fullManagerDto));
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc.perform(get("/api/manager/all")
                        .content(mapper.writeValueAsBytes(fullManagerDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is("Scarlett")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].role", is("MANAGER")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is("Scarlett")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].position", is("Custom Manager")));

        verify(managerService, times(1)).getAllManager();

    }

    @DisplayName("CREATE manager -- positive scenario ")
    @Test
    void createManager_positiveTest() throws Exception {

        ManagerCreateDto createdManager = new ManagerCreateDto("Scarlett", "25", "MANAGER", "Custom Manager", "10000");
        when(managerService.createManager(createdManager)).thenReturn(Optional.of(fullManagerDto));
        when(managerService.getManagerByName("Scarlett")).thenThrow(NoSuchElementException.class);

        mockMvc.perform(post("/api/manager/create")
                        .content(toJson(createdManager))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(createdManager.getName() + " -- A new manager with name{} is created"));
    }

    @DisplayName("CREATE manager -- negative scenario")
    @Test
    void createManager_negativeTest() throws Exception {

        ManagerCreateDto createdManager = new ManagerCreateDto("Scarlett", "25", "MANAGER", null, "10000");
        lenient().when(managerService.createManager(createdManager)).thenReturn(Optional.of(fullManagerDto));
        lenient().when(managerService.getManagerByName("Scarlett")).thenReturn(Optional.of(fullManagerDto));

        mockMvc.perform(post("/api/manager/create")
                        .content(toJson(createdManager))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fields", hasItem("position")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasItem("must not be blank")));

        verify(managerService, times(0)).createManager(createdManager);
    }

    @DisplayName("UPDATE manager by email -- positive scenario")
    @Test
    void updateUser_positiveTest() throws Exception {
        ManagerRequestDto updateManagerDto = new ManagerRequestDto();

        updateManagerDto.setPosition("Custom Service Manager");
        updateManagerDto.setName("Scarletta");
        fullManagerDto.setPosition("Custom Service Manager");
        fullManagerDto.setRole("Scarletta");
        when(managerService.updateManager(updateManagerDto, "Scarlett")).thenReturn(updateManagerDto);

        mockMvc.perform(patch("/api/manager/update/{name}", "Scarlett")
                        .content(toJson(updateManagerDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Scarlett Manager with the name {} is updated successfully"));
    }

    @DisplayName("DEACTIVATE user-- positive scenario")
    @Test
    void deactivateManager_positiveTest() throws Exception {

        when(managerService.deactivateManager(fullManagerDto.getName())).thenReturn(fullManagerDto);
        when(managerService.isStatusActive(fullManagerDto.getName())).thenReturn(true);

        mockMvc.perform(delete("/api/manager/deactivate/" + fullManagerDto.getName()))
                .andExpect(status().isOk())
                .andExpect(content().string(fullManagerDto.getName() + " –- Manager's status with a name {} is changed to INACTIVE"));
    }
    @DisplayName("DEACTIVATE manager-- negative scenario")
    @Test
    void deactivateManager_negativeTest() throws Exception {
        when(managerService.isStatusActive(fullManagerDto.getName())).thenReturn(false);
        mockMvc.perform(delete("/api/manager/deactivate/{name}", fullManagerDto.getName()))
                .andExpect(status().isConflict())
                .andExpect(content().string(fullManagerDto.getName() + " –- Manager with a name{} is already INACTIVE"));
    }
}
