package com.epam.zelener.restaurant.controllers;

import com.epam.zelener.restaurant.dtos.FoodRequestDto;
import com.epam.zelener.restaurant.dtos.FullFoodDto;
import com.epam.zelener.restaurant.services.FoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @PostMapping("/create")
    public void createFood(@Valid @RequestBody FoodRequestDto foodRequestDto) {
        log.info("Request to create a new Food :{}", foodRequestDto);
        foodService.createFood(foodRequestDto);
    }

    @DeleteMapping(value = "/{title}")
    public ResponseEntity<Void> deleteFood(@PathVariable String title) {
        log.info("Request to delete a Food with a title:{}", title);
        foodService.deleteFood(title);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public List<FullFoodDto> findAllFood() {
        log.info("Request to find all FullFoodDto :{}");
        return foodService.getAllFood();
    }

    @GetMapping("/get/{title}")
    public FoodRequestDto getFoodByTitle(@PathVariable String title) {
        log.info("Request to get a Food by a title:{}", title);
        return foodService.getFoodByTitle(title);
    }

    @PutMapping(value = "/{title}")
    public void updateFood(@Valid @RequestBody FoodRequestDto foodRequestDto, @PathVariable String title) {
        log.info("Request to updateFood with a title :{}", title);
        foodService.updateFood(foodRequestDto, title);
    }
}
