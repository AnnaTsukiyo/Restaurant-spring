package com.epam.zelener.restaurant.controllers;

import com.epam.zelener.restaurant.dtos.FullUserDto;
import com.epam.zelener.restaurant.dtos.UserCreateDto;
import com.epam.zelener.restaurant.dtos.UserUpdateDto;
import com.epam.zelener.restaurant.exceptions.UserNotFoundSuchElementException;
import com.epam.zelener.restaurant.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Optional;

import static com.epam.zelener.restaurant.model.Role.CUSTOMER;
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
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private FullUserDto fullUserDto;

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @BeforeEach
    void init() {
        fullUserDto = new FullUserDto("1", "Jonas Kahnwald", "+3806567898", "GUEST", "Winden", "ACTIVE", "2000-01-01", "jonas@gmail.com", "passwordP1", "passwordP1");
    }

    @DisplayName("CREATE user -- positive scenario ")
    @Test
    void createUser_positiveTest() throws Exception {

        UserCreateDto createdUser = new UserCreateDto("Jonas Kahnwald", "CUSTOMER", "jonasK@gmail.com", "+38076543401", "2001-01-01", "passwordP123", "passwordP123");
        when(userService.createUser(createdUser)).thenReturn(Optional.of(fullUserDto));
        when(userService.getUserByEmail("jonasK@gmail.com")).thenThrow(UserNotFoundSuchElementException.class);
        when(userService.getUserByEmail("+38076543401")).thenThrow(UserNotFoundSuchElementException.class);

        mockMvc.perform(post("/api/user/create").content(toJson(createdUser)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().string(createdUser.getEmail() + ": user was created"));
        verify(userService, times(1)).getUserByEmail("jonasK@gmail.com");
    }

    @DisplayName("CREATE user -- negative scenario")
    @Test
    void createUser_checkValidation_negativeTest() throws Exception {

        UserCreateDto createdUser = new UserCreateDto("Jordan Merryland", String.valueOf(CUSTOMER), "jonas$@gmail.com", null, "2000-01-01", "passwordP1", "passwordP1");
        when(userService.createUser(createdUser)).thenReturn(Optional.of(fullUserDto));
        when(userService.getUserByEmail("jonas$@gmail.com")).thenReturn(Optional.of(fullUserDto));
        System.out.println(createdUser);
        mockMvc.perform(post("/api/user/create").
                        content(toJson(createdUser)).
                        header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(MockMvcResultMatchers.jsonPath("$.status", is(400))).
                andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasItem("must not be null")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fields", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fields", hasItem("phoneNumber")));

        verify(userService, times(1)).getUserByEmail("jonas$@gmail.com");
        verify(userService, times(0)).createUser(createdUser);
    }

    @DisplayName("GET user by email -- positive scenario")
    @Test
    void getUserByEmail_positiveTest() throws Exception {

        lenient().when(userService.getUserByEmail("jonas@gmail.com")).thenReturn(Optional.ofNullable(fullUserDto));
        mapper.writeValueAsString(fullUserDto);
        mockMvc.perform(get("/api/user/get/jonas@gmail.com").content(mapper.writeValueAsBytes(fullUserDto)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.role", is("GUEST"))).andExpect(MockMvcResultMatchers.jsonPath("$.status", is("ACTIVE"))).andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth", is("2000-01-01"))).andDo(print());
    }

    @DisplayName("UPDATE user by email -- positive scenario")
    @Test
    void updateUser_positiveTest() throws Exception {
        UserUpdateDto updateUserDto = new UserUpdateDto();

        updateUserDto.setPassword("PasswordsQwert1");
        updateUserDto.setFullName("Johannes Kahnwald");
        fullUserDto.setPassword("PasswordsQwert1");
        fullUserDto.setFullName("Johannes Kahnwald");
        when(userService.updateUser(updateUserDto, "jonas@gmail.com")).thenReturn(updateUserDto);

        mockMvc.perform(patch("/api/user/update/{email}", "jonas@gmail.com").content(toJson(updateUserDto)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().string("Johannes Kahnwald --User with a given email is updated successfully"));
    }

    @DisplayName("GET user by email -- negative scenario")
    @Test
    void getByEmail_negativeTest() throws Exception {
        lenient().when(userService.getUserByEmail("marta@gmail.com")).thenThrow(UserNotFoundSuchElementException.class);
        mockMvc.perform(get("/api/user/get/marta@gmail.com")).andExpect(status().isNotFound()).andExpect(content().string("marta@gmail.com -- User with such email {} doesn't exist "));
        verify(userService, times(1)).getUserByEmail("marta@gmail.com");
    }

    @DisplayName("GET all users -- positive scenario")
    @Test
    void getAllUsers_positiveTest() throws Exception {
        lenient().when(userService.getAllUsers()).thenReturn(List.of(fullUserDto));

        mockMvc.perform(get("/api/user/all").content(mapper.writeValueAsBytes(fullUserDto)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$[0].fullName", is("Jonas Kahnwald"))).andExpect(MockMvcResultMatchers.jsonPath("$[0].role", is("GUEST"))).andExpect(MockMvcResultMatchers.jsonPath("$[0].status", is("ACTIVE"))).andExpect(MockMvcResultMatchers.jsonPath("$[0].dateOfBirth", is("2000-01-01")));

        verify(userService, times(1)).getAllUsers();

    }

    @DisplayName("DEACTIVATE user-- positive scenario")
    @Test
    void deactivateUser_positiveTest() throws Exception {

        when(userService.deactivateUser(fullUserDto.getEmail())).thenReturn(fullUserDto);
        when(userService.isStatusActive(fullUserDto.getEmail())).thenReturn(true);

        mockMvc.perform(delete("/api/user/deactivate/" + fullUserDto.getEmail())).andExpect(status().isOk()).andExpect(content().string(fullUserDto.getEmail() + " –- User's status with an email {} is changed to INACTIVE"));
    }

    @DisplayName("DEACTIVATE user-- negative scenario")
    @Test
    void deactivateUser_negativeTest() throws Exception {
        when(userService.isStatusActive(fullUserDto.getEmail())).thenReturn(false);

        mockMvc.perform(delete("/api/user/deactivate/{email}", fullUserDto.getEmail())).andExpect(status().isConflict()).andExpect(content().string(fullUserDto.getEmail() + " –- User with such email is already INACTIVE"));
    }

    @DisplayName("GET all users by pages-- positive scenario")
    @Test
    void getAllByPages_positiveTest() throws Exception {

        FullUserDto userDtoMarta = new FullUserDto();
        userDtoMarta.setFullName("Marta Norton");
        FullUserDto userDtoPaul = new FullUserDto();
        userDtoPaul.setFullName("Paul Newman");
        when(userService.getAllByPages("0")).thenReturn(List.of(fullUserDto, userDtoMarta, userDtoPaul));

        mockMvc.perform(get("/api/user/all/0")).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$[0].fullName", is("Jonas Kahnwald"))).andExpect(MockMvcResultMatchers.jsonPath("$[1].fullName", is("Marta Norton"))).andExpect(MockMvcResultMatchers.jsonPath("$[2].fullName", is("Paul Newman")));
    }
}

