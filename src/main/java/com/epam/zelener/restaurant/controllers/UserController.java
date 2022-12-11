package com.epam.zelener.restaurant.controllers;

import com.epam.zelener.restaurant.dtos.FullUserDto;
import com.epam.zelener.restaurant.dtos.UserCreateDto;
import com.epam.zelener.restaurant.dtos.UserUpdateDto;
import com.epam.zelener.restaurant.exceptions.UserNotFoundSuchElementException;
import com.epam.zelener.restaurant.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "A new user is created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data is provided. User is not created.", content = @Content)})
    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Object User is to be created") @RequestBody @Valid UserCreateDto userCreateDto) {
        log.info("Request to create a new User :{}", userCreateDto);
        userService.createUser(userCreateDto);
        return new ResponseEntity<>(userCreateDto.getEmail() + ": user was created", HttpStatus.OK);
    }

    @Operation(summary = "Delete a user by changing status to INACTIVE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's status is changed to INACTIVE successfully"),
            @ApiResponse(responseCode = "404", description = "User is not found"),
            @ApiResponse(responseCode = "409", description = "User is already INACTIVE")})
    @DeleteMapping(value = "/deactivate/{email}")
    public ResponseEntity<Object> deactivateUser(@PathVariable("email") String email) {
        try {
            if (!userService.isStatusActive(email)) {
                log.warn("User with an email is already INACTIVE!!!");
                return new ResponseEntity<>(email + " –- User with such email is already INACTIVE", HttpStatus.CONFLICT);
            } else {
                userService.deactivateUser(email);
                log.info("Request to deactivate a User with the email :{}", email);
                return new ResponseEntity<>(email + " –- User's status with an email {} is changed to INACTIVE", HttpStatus.OK);
            }
        } catch (UserNotFoundSuchElementException e) {
            log.error("User with a given email {} doesn't exist", email);
            return new ResponseEntity<>(email + " -- User with such email doesn't exist", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found all users in database successfully"),
            @ApiResponse(responseCode = "404", description = "No users are found. No users data!")})
    @GetMapping("/all")
    public ResponseEntity<Object> findAllUsers() {
        log.info("Request to find all FullDishDto :");
        List<FullUserDto> userList = userService.getAllUsers();
        if (!userList.isEmpty()) {
            log.info("Users are found successfully");
            return new ResponseEntity<>(userList, HttpStatus.OK);
        } else {
            log.error("No users are found. No users data!");
            return new ResponseEntity<>("No users data!", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get a user by its email")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found the user", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FullUserDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid email provided", content = @Content),
            @ApiResponse(responseCode = "404", description = "User is not found", content = @Content)})
    @GetMapping("/get/{email}")
    public ResponseEntity<Object> getUserByEmail(@Parameter(description = "email of the user to be searched") @PathVariable String email) {
        try {
            if (userService.getUserByEmail(email).isEmpty()) {
                log.warn("There is no user with a given email : {} !", email);
                return new ResponseEntity<>(email + " –- Invalid email provided.", HttpStatus.BAD_REQUEST);
            } else {
                Optional<FullUserDto> user = userService.getUserByEmail(email);
                log.info("Request to get a FullSignUpDto by the email :{}", email);
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
        } catch (UserNotFoundSuchElementException e) {
            log.error("User with such email {} doesn't exist", email);
            return new ResponseEntity<>(email + " -- User with such email {} doesn't exist ", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Update user by its email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The user is updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid email is provided"),
            @ApiResponse(responseCode = "404", description = "User is not found")})
    @PatchMapping(value = "/update/{email}")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserUpdateDto userUpdateDto, @PathVariable("email") String email) {
        try {
            userService.updateUser(userUpdateDto, email);
            log.info("Request to update User with email:{}", email);
            return new ResponseEntity<>(userUpdateDto.getFullName() + " --User with email is updated successfully", HttpStatus.OK);
        } catch (UserNotFoundSuchElementException e) {
            log.error("User with such email was not found");
            return new ResponseEntity<>("User with such email was not found " + email, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get all users By Pages")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found users", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FullUserDto.class))}), @ApiResponse(responseCode = "404", description = "Users are not found", content = @Content)})
    @GetMapping("/all/{page}")
    public ResponseEntity<Object> getAllByPages(@PathVariable String page) {
        log.info("getting all users");
        return new ResponseEntity<>(userService.getAllByPages(page), HttpStatus.OK);
    }
}
