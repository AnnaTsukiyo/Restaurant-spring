package com.epam.zelener.restaurant.controllers;

import com.epam.zelener.restaurant.dtos.DishRequestDto;
import com.epam.zelener.restaurant.dtos.FullDishDto;
import com.epam.zelener.restaurant.exceptions.DishNotFoundSuchElementException;
import com.epam.zelener.restaurant.model.Categories;
import com.epam.zelener.restaurant.services.DishService;
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
@RequestMapping("/api/dish")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @Operation(summary = "Create a new dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A new dish is created",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data is provided. Dish is not created!",
                    content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<Object> createDish(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Object User is to be created")
                                             @RequestBody @Valid DishRequestDto dishRequestDto) {
        log.info("Request to create a new Dish :{}", dishRequestDto);
        dishService.createDish(dishRequestDto);
        return new ResponseEntity<>(dishRequestDto.getTitle() + " -- A new dish with a title {} is created", HttpStatus.OK);
    }

    @Operation(summary = "Delete a dish from a database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish is successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Dish is not found"),
    })
    @DeleteMapping(value = "/delete/{title}")
    public ResponseEntity<Object> deleteDish(@PathVariable String title) {
        try {
            log.info("Request to delete a Dish with a title:{}", title);
            dishService.deleteDish(title);
            return new ResponseEntity<>(title + " – Dish with a title{} is successfully deleted", HttpStatus.OK);
        } catch (DishNotFoundSuchElementException e) {
            log.error("Dish with a given title {} doesn't exist", title);
            return new ResponseEntity<>(title + " -- Dish with such title doesn't exist", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get all dishes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all dishes in database"),
            @ApiResponse(responseCode = "404", description = "No dishes are found. No dishes data!")})
    @GetMapping("/all")
    public ResponseEntity<Object> findAllDish() {
        log.info("Request to find all FullDishDto :");
        List<FullDishDto> dishesList = dishService.getAllDish();
        if (!dishesList.isEmpty()) {
            log.info("All dishes are found!");
            return new ResponseEntity<>("Found all dishes in database", HttpStatus.OK);
        } else {
            log.error("No dishes are found. No dishes data!");
            return new ResponseEntity<>("No dishes data!", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get a dish by its title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the dish successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DishRequestDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid title provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Dish is not found",
                    content = @Content)})
    @GetMapping("/get/{title}")
    public ResponseEntity<Object> getDishByTitle(@Parameter(description = "title of the dish to be searched")
                                                 @PathVariable String title) {
        try {
            if (dishService.getDishByTitle(title).getTitle().isEmpty() || dishService.getDishByTitle(title).getTitle() == null) {
                log.warn("There is no dish with a given title : {} !", title);
                return new ResponseEntity<>(title + " –- Invalid title provided.", HttpStatus.BAD_REQUEST);
            }
            dishService.getDishByTitle(title);
            log.info("Request to get a DishRequestDto by the title :{}", title);
            return new ResponseEntity<>(title + "-- Dish with a given title {} is found successfully", HttpStatus.OK);

        } catch (DishNotFoundSuchElementException e) {
            log.error("No dish is found!");
            return new ResponseEntity<>(title + " -- Dish with such email {} doesn't exist ", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Update dish by its title")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "The dish is updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid title provided"),
            @ApiResponse(responseCode = "404", description = "Dish is not found")})
    @PatchMapping(value = "/{title}")
    public ResponseEntity<Object> updateDish(@Valid @RequestBody DishRequestDto dishRequestDto, @PathVariable String title) {
        try {
            dishService.updateDish(dishRequestDto, title);
            log.info("Request to update Dish with a title:{}", title);
            return new ResponseEntity<>(dishRequestDto.getTitle() + " -- Dish with a given title {} is updated successfully", HttpStatus.OK);
        } catch (DishNotFoundSuchElementException e) {
            log.error("No dish is found!");
            return new ResponseEntity<>(dishRequestDto.getTitle() + "-- Dish with such  doesn't exist ", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Sorting dishes by pages")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Dishes are sorted by pages successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data provided")
    })
    @GetMapping("/sort/by/{sort}/{page}")
    public ResponseEntity<Object> sortBy(@PathVariable String sort, @PathVariable String page) {
        if (sort.equals("price") || sort.equals("title")) {
            log.info("sorting all dishes");
            return new ResponseEntity<>(dishService.sortingBy(sort, page), HttpStatus.OK);
        } else {
            log.warn("Incorrect sorting type provided. Dishes can be sorted by price or title");
            return new ResponseEntity<>("Incorrect sorting type (must be price or title)", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Getting dishes by category")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "All dishes by category are extracted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid category data provided")
    })
    @GetMapping("/get/by/{category}/{page}")
    public ResponseEntity<Object> getByCategory(@PathVariable String category, @PathVariable String page) {
        try {
            Categories.valueOf(category);
            log.info("All dishes are extracted by the given category", category);
            return new ResponseEntity<>(dishService.getByCategory(category, page), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("The given category is not found", category);
            return new ResponseEntity<>(category + " -- Given category {} is not found", HttpStatus.NOT_FOUND);
        }
    }
}