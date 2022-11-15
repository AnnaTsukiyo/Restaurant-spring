package com.epam.zelener.restaurant.controllers;

import com.epam.zelener.restaurant.dtos.IngredientRequestDto;
import com.epam.zelener.restaurant.services.IngredientService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/ingredient")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @PostMapping("/create")
    public void createIngredient(@Valid @RequestBody IngredientRequestDto ingredientRequestDto) {
        log.info("Request to create a new Ingredient :{}", ingredientRequestDto);
        ingredientService.createIngredient(ingredientRequestDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable String id) {
        log.info("Request to delete a Ingredient with a id :{}", id);
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public List<IngredientRequestDto> getAll() {
        log.info("Request to  get all IngredientRequestDto :{}");
        return ingredientService.getAllIngredients();
    }

    @GetMapping("/get/{id}")
    public IngredientRequestDto getById(String id) {
        log.info("Request to get an Ingredient by id :{}", id);
        return ingredientService.getIngredientById(id);
    }

    @PutMapping(value = "/{id}")
    public void update(@Valid @RequestBody IngredientRequestDto ingredientRequestDto, @PathVariable String id) {
        log.info("Request to update Ingredient :{}", id);
        ingredientService.updateIngredient(ingredientRequestDto, id);
    }
}
