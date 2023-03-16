package com.epam.zelener.restaurant.controllers;

import com.epam.zelener.restaurant.dtos.FullRecipeDto;
import com.epam.zelener.restaurant.dtos.RecipeCreateDto;
import com.epam.zelener.restaurant.dtos.RecipeRequestDto;
import com.epam.zelener.restaurant.services.RecipeService;
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
@RequestMapping("/api/recipe")
public class RecipeController {

    @Resource
    private RecipeService recipeService;

    @Operation(summary = "Create a new recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A new recipe is created",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data is provided. Recipe is not created!",
                    content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<Object> createRecipe(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Object Ingredient is to be created")
                                               @Valid @RequestBody RecipeCreateDto recipeRequestDto) {
        log.info("Request to create a new Recipe :{}", recipeRequestDto);
        recipeService.createRecipe(recipeRequestDto);
        return new ResponseEntity<>(recipeRequestDto.getTitle() + " -- A new recipe with a title {} is created", HttpStatus.OK);
    }

    @Operation(summary = "Deactivate a recipe in a database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe is successfully deactivated"),
            @ApiResponse(responseCode = "404", description = "Recipe is not found"),
    })
    @DeleteMapping(value = "/deactivate/{title}")
    public ResponseEntity<Object> deactivateRecipe(@PathVariable String title) {
        log.info("Request to deactivate a Recipe with a title:{}", title);
        try {
            if (!recipeService.isStatusActive(title)) {
                log.warn("Recipe with title is already INACTIVE!!!");
                return new ResponseEntity<>(title + " –- Recipe with such title is already INACTIVE", HttpStatus.CONFLICT);
            } else {
                recipeService.deactivateRecipe(title);
                return new ResponseEntity<>(title + " – Recipe with a title{} is successfully deactivated", HttpStatus.OK);
            }
        } catch (NoSuchElementException e) {
            log.error("Recipe with a given title {} doesn't exist", title);
            return new ResponseEntity<>(title + " -- Recipe with such title doesn't exist", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get all food")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all food in database"),
            @ApiResponse(responseCode = "404", description = "No foods are found. No foods data!")})
    @GetMapping("/all")
    public ResponseEntity<Object> findAllRecipe() {
        log.info("Request to find all FullRecipeDto :");
        List<FullRecipeDto> recipesList = recipeService.getAllRecipe();
        if (!recipesList.isEmpty()) {
            log.info("All recipes are found!");
            return new ResponseEntity<>(recipesList, HttpStatus.OK);
        } else {
            log.error("No recipes are found. No recipes data!");
            return new ResponseEntity<>("No recipes data!", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get a recipe by its title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the recipe successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeRequestDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid title provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Recipe is not found",
                    content = @Content)})
    @GetMapping("/get/{title}")
    public ResponseEntity<Object> getRecipeByTitle(@Parameter(description = "title of the recipe to be searched")
                                                   @PathVariable String title) {
        log.info("Request to get Recipe by a title:{}", title);
        try {
            if (recipeService.getRecipeByTitle(title).isEmpty()) {
                log.warn("There is no recipe with a given title : {} !", title);
                return new ResponseEntity<>(title + " –- Invalid title provided.", HttpStatus.BAD_REQUEST);
            }
            Optional<FullRecipeDto> recipeDto = recipeService.getRecipeByTitle(title);
            log.info("Request to get a RecipeRequestDto by the title :{}", title);
            return new ResponseEntity<>(recipeDto, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            log.error("No Recipe is found!");
            return new ResponseEntity<>(title + " -- Recipe with such email {} doesn't exist ", HttpStatus.NOT_FOUND);
        }
    }


    @Operation(summary = "Update recipe by its title")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Recipe is updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid title provided"),
            @ApiResponse(responseCode = "404", description = "Recipe is not found")})
    @PatchMapping(value = "/update/{title}")
    public ResponseEntity<Object> updateRecipe(@Valid @RequestBody RecipeRequestDto recipeRequestDto, @PathVariable String title) {
        log.info("Request to updateRecipe with a title :{}", title);
        try {
            recipeService.updateRecipe(recipeRequestDto, title);
            return new ResponseEntity<>(title + " -- Recipe with a given title {} is updated successfully", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            log.error("No recipe is found!");
            return new ResponseEntity<>(recipeRequestDto.getTitle() + "-- Recipe with such title{} doesn't exist ", HttpStatus.NOT_FOUND);
        }
    }
}
