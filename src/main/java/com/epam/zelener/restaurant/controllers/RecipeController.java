package com.epam.zelener.restaurant.controllers;

import com.epam.zelener.restaurant.dtos.FullRecipeDto;
import com.epam.zelener.restaurant.dtos.RecipeRequestDto;
import com.epam.zelener.restaurant.services.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping("/create")
    public void createRecipe(@Valid @RequestBody RecipeRequestDto recipeRequestDto) {
        log.info("Request to create a new Recipe :{}", recipeRequestDto);
        recipeService.createRecipe(recipeRequestDto);
    }

    @DeleteMapping(value = "/{title}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable String title) {
        log.info("Request to delete a Recipe with a title:{}", title);
        recipeService.deleteRecipe(title);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public List<FullRecipeDto> findAllRecipe() {
        log.info("Request to find all FullRecipeDto :{}");
        return recipeService.getAllRecipe();
    }

    @GetMapping("/get/{title}")
    public RecipeRequestDto getRecipeByTitle(@PathVariable String title) {
        log.info("Request to get a Recipe by a title:{}", title);
        return recipeService.getRecipeByTitle(title);
    }

    @PutMapping(value = "/{title}")
    public void updateRecipe(@Valid @RequestBody RecipeRequestDto recipeRequestDto, @PathVariable String title) {
        log.info("Request to updateRecipe with a title :{}", title);
        recipeService.updateRecipe(recipeRequestDto, title);
    }
}
