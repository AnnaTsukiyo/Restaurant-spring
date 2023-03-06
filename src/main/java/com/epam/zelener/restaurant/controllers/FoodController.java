package com.epam.zelener.restaurant.controllers;

import com.epam.zelener.restaurant.dtos.FoodCreateDto;
import com.epam.zelener.restaurant.dtos.FoodRequestDto;
import com.epam.zelener.restaurant.dtos.FullFoodDto;
import com.epam.zelener.restaurant.exceptions.DishNotFoundSuchElementException;
import com.epam.zelener.restaurant.services.FoodService;
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
import java.util.NoSuchElementException;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/api/food")
public class FoodController {

    @Resource
    private FoodService foodService;

    @Operation(summary = "Create a new food")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A new food is created",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data is provided. Food is not created!",
                    content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<Object> createFood(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Object Food is to be created")
                                             @Valid @RequestBody FoodCreateDto foodCreateDto) {
        log.info("Request to create a new Food :{}", foodCreateDto);
        foodService.createFood(foodCreateDto);
        return new ResponseEntity<>(foodCreateDto.getTitle() + " -- A new food with a title {} is created", HttpStatus.OK);
    }

    @Operation(summary = "Deactivate a food  by changing status to INACTIVE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food is successfully deactivated"),
            @ApiResponse(responseCode = "404", description = "Food is not found"),
    })
    @DeleteMapping(value = "/deactivate/{title}")
    public ResponseEntity<Object> deactivateFood(@PathVariable ("title") String title) {
        log.info("Request to deactivate a Food with a title:{}", title);
        try {
            if (!foodService.isStatusActive(title)) {
                log.warn("Food with title is already INACTIVE!!!");
            return new ResponseEntity<>(title + " – Food with such title is already INACTIVE", HttpStatus.CONFLICT);
            } else {
                foodService.deactivateFood(title);
                log.info("Request to deactivate Dish with the title :{}", title);
                return new ResponseEntity<>(title + " –- Food status with title {} is changed to INACTIVE", HttpStatus.OK);
            }
        } catch (NoSuchElementException e) {
            log.error("Food with a given title {} doesn't exist", title);
            return new ResponseEntity<>(title + " -- Food with such title doesn't exist", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get all food")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all food in database"),
            @ApiResponse(responseCode = "404", description = "No foods are found. No foods data!")})
    @GetMapping("/all")
    public ResponseEntity<Object> findAllFood() {
        log.info("Request to find all FullFoodDto :");
        List<FullFoodDto> foods = foodService.getAllFood();
        if (!foods.isEmpty()) {
            log.info("All foods are found!");
            return new ResponseEntity<>(foods, HttpStatus.OK);
        } else {
            log.error("No foods are found. No foods data!");
            return new ResponseEntity<>("No foods data!", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get a food by its title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the food successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FoodRequestDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid title provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Food is not found",
                    content = @Content)})
    @GetMapping("/get/{title}")
    public ResponseEntity<Object> getFoodByTitle(@Parameter(description = "title of the food to be searched")
                                                 @PathVariable String title) {
        try {
            if (foodService.getFoodByTitle(title).isEmpty() || foodService.getFoodByTitle(title) == null) {
                log.warn("There is no food with a given title : {} !", title);
                return new ResponseEntity<>(title + " –- Invalid title provided.", HttpStatus.BAD_REQUEST);
            }
            log.info("Request to get a Food by a title:{}", title);
            Optional<FullFoodDto> food = foodService.getFoodByTitle(title);
            return new ResponseEntity<>(food, HttpStatus.OK);

        } catch (NoSuchElementException e) {
            log.error("No food is found!");
            return new ResponseEntity<>(title + " -- Food with such title {} doesn't exist!", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Update food by its title")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "The food is updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid title provided"),
            @ApiResponse(responseCode = "404", description = "Food is not found")})
    @PatchMapping(value = "/{title}")
    public ResponseEntity<Object> updateFood(@Valid @RequestBody FoodRequestDto foodRequestDto, @PathVariable String title) {
        try {
            log.info("Request to updateFood with a title :{}", title);
            foodService.updateFood(foodRequestDto, title);
            return new ResponseEntity<>(title + " -- Food with a given title {} is updated successfully", HttpStatus.OK);
        } catch (DishNotFoundSuchElementException e) {
            log.error("No food is found!");
            return new ResponseEntity<>(foodRequestDto.getTitle() + "-- Food with such title{} doesn't exist ", HttpStatus.NOT_FOUND);
        }
    }
}
