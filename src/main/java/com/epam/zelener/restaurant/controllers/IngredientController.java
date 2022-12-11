package com.epam.zelener.restaurant.controllers;

import com.epam.zelener.restaurant.dtos.FullIngredientDto;
import com.epam.zelener.restaurant.dtos.IngredientCreateDto;
import com.epam.zelener.restaurant.dtos.IngredientRequestDto;
import com.epam.zelener.restaurant.exceptions.DishNotFoundSuchElementException;
import com.epam.zelener.restaurant.services.IngredientService;
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
@RequestMapping("/api/ingredient")
public class IngredientController {

    @Resource
    private IngredientService ingredientService;

    @Operation(summary = "Create a new ingredient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A new ingredient is created",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data is provided. Ingredient is not created!",
                    content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<Object> createIngredient(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Object Ingredient is to be created")
                                                   @Valid @RequestBody IngredientCreateDto ingredientRequestDto) {
        log.info("Request to create a new Ingredient :{}", ingredientRequestDto);
        ingredientService.createIngredient(ingredientRequestDto);
        return new ResponseEntity<>(ingredientRequestDto.getId() + " -- A new ingredient with id {} is created", HttpStatus.OK);
    }

    @Operation(summary = "Delete ingredient from a database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingredient is successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Ingredient is not found"),
    })
    @DeleteMapping("/deactivate/{id}")
    public ResponseEntity<Object> deactivateIngredient(@PathVariable String id) {
        log.info("Request to deactivate an Ingredient with a id :{}", id);
        try {
            if (!ingredientService.isStatusActive(id)) {
                log.warn("Ingredient with id is already INACTIVE!!!");
                return new ResponseEntity<>(id + " -– Ingredient with such id is already INACTIVE", HttpStatus.CONFLICT);
            } else {
                ingredientService.deactivateIngredient(id);
                log.info("Request to deactivate Ingredient with the id :{}", id);
                return new ResponseEntity<>(id + " –- Ingredient with an id{} is successfully deactivated", HttpStatus.OK);
            }
        } catch (NoSuchElementException e) {
            log.error("Ingredient with a given id {} doesn't exist", id);
            return new ResponseEntity<>(id + " -- Ingredient with such id doesn't exist", HttpStatus.NOT_FOUND);
        }
    }


        @Operation(summary = "Get all food")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Found all food in database"),
                @ApiResponse(responseCode = "404", description = "No foods are found. No foods data!")})
        @GetMapping("/all")
        public ResponseEntity<Object> findAllIngredients () {
            log.info("Request to  get all IngredientRequestDto :");
            List<FullIngredientDto> ingredients = ingredientService.getAllIngredients();
            if (!ingredients.isEmpty()) {
                log.info("All ingredients are found!");
                return new ResponseEntity<>(ingredients, HttpStatus.OK);
            } else {
                log.error("No ingredients are found. No ingredients data!");
                return new ResponseEntity<>("No ingredients data!", HttpStatus.NOT_FOUND);
            }
        }

        @Operation(summary = "Get a ingredient by its id")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Found the ingredients successfully",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = FullIngredientDto.class))}),
                @ApiResponse(responseCode = "400", description = "Invalid id provided",
                        content = @Content),
                @ApiResponse(responseCode = "404", description = "Ingredient is not found",
                        content = @Content)})
        @GetMapping("/get/{id}")
        public ResponseEntity<Object> getById (@Parameter(description = "Id of the ingredient to be searched") @PathVariable String id){
            try {
                if (ingredientService.getIngredientById(id).isEmpty() || ingredientService.getIngredientById(id) == null) {
                    log.warn("There is no ingredient with a given id : {} !", id);
                    return new ResponseEntity<>(id + " –- Invalid id provided.", HttpStatus.BAD_REQUEST);
                }
                Optional<FullIngredientDto> ingredient = ingredientService.getIngredientById(id);
                log.info("Request to get ingredient by a id:{}", id);
                return new ResponseEntity<>(ingredient, HttpStatus.OK);

            } catch (NoSuchElementException e) {
                log.error("No food is found!");
                return new ResponseEntity<>(id + " -- Ingredient with such id {} doesn't exist ", HttpStatus.NOT_FOUND);
            }
        }

        @Operation(summary = "Update ingredient by its title")
        @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Ingredient is updated successfully"),
                @ApiResponse(responseCode = "400", description = "Invalid id provided"),
                @ApiResponse(responseCode = "404", description = "Ingredient is not found")})
        @PatchMapping(value = "/update/{id}")
        public ResponseEntity<Object> updateIngredient (@Valid @RequestBody IngredientRequestDto
        ingredientRequestDto, @PathVariable("id") String id){
            try {
                log.info("Request to update Ingredient :{}", id);
                ingredientService.updateIngredient(ingredientRequestDto, id);
                return new ResponseEntity<>(id + " -- Ingredient with a given id {} is updated successfully", HttpStatus.OK);
            } catch (DishNotFoundSuchElementException e) {
                log.error("No ingredient is found!");
                return new ResponseEntity<>(ingredientRequestDto.getId() + " -- Ingredient with such id{} doesn't exist ", HttpStatus.NOT_FOUND);
            }
        }
    }
