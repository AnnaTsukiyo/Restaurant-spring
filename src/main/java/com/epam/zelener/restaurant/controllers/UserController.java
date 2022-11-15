package com.epam.zelener.restaurant.controllers;

import com.epam.zelener.restaurant.dtos.FullUserDto;
import com.epam.zelener.restaurant.dtos.UserSignUpDto;
import com.epam.zelener.restaurant.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A new user is created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserSignUpDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data is provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User is not found",
                    content = @Content)})
    @PostMapping("/create")
    public void createUser(@RequestBody @Valid UserSignUpDto userSignUpDto) {
        log.info("Request to create a new User :{}", userSignUpDto);
        userService.createUser(userSignUpDto);
    }

    @DeleteMapping(value = "/delete/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        log.info("Request to delete a User with the email :{}", email);
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found users",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserSignUpDto.class))}),
            @ApiResponse(responseCode = "404", description = "Users are not found",
                    content = @Content)})
    @GetMapping("/all")
    public List<FullUserDto> findAllUsers() {
        log.info("Request to find all FullDishDto :");
        return userService.getAllUsers();
    }

    @Operation(summary = "Get a user by its email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserSignUpDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid email supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User is not found",
                    content = @Content)})
    @GetMapping("/{email}")
    public ResponseEntity<Object>  getUserByEmail(@Parameter(description = "email of the user to be searched")
                                        @PathVariable String email) {
        log.info("Request to get a UserSignUpDto by the email :{}", email);
        return new ResponseEntity<> (userService.getUserByEmail(email),HttpStatus.OK);
    }

    @Operation(summary = "Update user by its email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The user is updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserSignUpDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid email supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User is not found",
                    content = @Content)})
    @PutMapping(value = "/{email}")
    public void updateUser(@Valid @RequestBody UserSignUpDto userSignUpDto, @PathVariable String email) {
        log.info("Request to update a User with a email:{}", email);
        userService.updateUser(userSignUpDto, email);
    }

    @Operation(summary = "Get all users By Pages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found users",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserSignUpDto.class))}),
            @ApiResponse(responseCode = "404", description = "Users are not found",
                    content = @Content)})
    @GetMapping("/all/{page}")
    public ResponseEntity<Object> getAllByPages(@PathVariable String page) {
        log.info("getting all users");
        return new ResponseEntity<>(userService.getAllByPages(page), HttpStatus.OK);
    }
}
