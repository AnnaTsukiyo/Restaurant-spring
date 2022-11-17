package com.epam.zelener.restaurant.controllers;

import com.epam.zelener.restaurant.dtos.FullRecipeDto;
import com.epam.zelener.restaurant.dtos.RecipeRequestDto;
import com.epam.zelener.restaurant.exceptions.DishNotFoundSuchElementException;
import com.epam.zelener.restaurant.services.RecipeService;
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
import java.util.NoSuchElementException;

@Log4j2
@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @Operation(summary = "Create a new recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A new recipe is created",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data is provided. Recipe is not created!",
                    content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<Object> createRecipe(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Object Ingredient is to be created")
                                               @Valid @RequestBody RecipeRequestDto recipeRequestDto) {
        log.info("Request to create a new Recipe :{}", recipeRequestDto);
        recipeService.createRecipe(recipeRequestDto);
        return new ResponseEntity<>(recipeRequestDto.getTitle() + " -- A new recipe with a title {} is created", HttpStatus.OK);
    }

    @Operation(summary = "Delete a recipe from a database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe is successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Recipe is not found"),
    })
    @DeleteMapping(value = "/delete/{title}")
    public ResponseEntity<Object> deleteRecipe(@PathVariable String title) {

        try {
            log.info("Request to delete a Recipe with a title:{}", title);
            recipeService.deleteRecipe(title);
            return new ResponseEntity<>(title + " – Recipe with a title{} is successfully deleted", HttpStatus.OK);
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
            return new ResponseEntity<>("Found all recipes in database", HttpStatus.OK);
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
        try {
            if (recipeService.getRecipeByTitle(title).getTitle().isEmpty() || recipeService.getRecipeByTitle(title).getTitle() == null) {
                log.warn("There is no recipe with a given title : {} !", title);
                return new ResponseEntity<>(title + " –- Invalid title provided.", HttpStatus.BAD_REQUEST);
            }
            log.info("Request to get Recipe by a title:{}", title);
            recipeService.getRecipeByTitle(title);
            return new ResponseEntity<>(title + "-- Recipe with a given title {} is found successfully", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            log.error("No Recipe is found!");
            return new ResponseEntity<>(title + " -- Recipe with such email {} doesn't exist ", HttpStatus.NOT_FOUND);
        }
    }


    @Operation(summary = "Update recipe by its title")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Recipe is updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid title provided"),
            @ApiResponse(responseCode = "404", description = "Recipe is not found")})
    @PutMapping(value = "/{title}")
    public ResponseEntity<Object> updateRecipe(@Valid @RequestBody RecipeRequestDto recipeRequestDto, @PathVariable String title) {
        try {
            log.info("Request to updateRecipe with a title :{}", title);
            recipeService.updateRecipe(recipeRequestDto, title);
            return new ResponseEntity<>(recipeRequestDto.getTitle() + " -- Recipe with a given title {} is updated successfully", HttpStatus.OK);
        } catch (DishNotFoundSuchElementException e) {
            log.error("No food is found!");
            return new ResponseEntity<>(recipeRequestDto.getTitle() + "-- Recipe with such title{} doesn't exist ", HttpStatus.NOT_FOUND);
        }
    }
}
