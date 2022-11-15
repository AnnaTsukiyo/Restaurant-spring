package com.epam.zelener.restaurant.controllers;

import com.epam.zelener.restaurant.dtos.DishRequestDto;
import com.epam.zelener.restaurant.dtos.FullDishDto;
import com.epam.zelener.restaurant.services.DishService;
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

    @PostMapping("/create")
    public void createDish(@RequestBody @Valid DishRequestDto dishRequestDto) {
        log.info("Request to create a new Dish :{}", dishRequestDto);
        dishService.createDish(dishRequestDto);
    }

    @DeleteMapping(value = "/{title}")
    public ResponseEntity<Void> deleteDish(@PathVariable String title) {
        log.info("Request to delete a Dish with a title :{}", title);
        dishService.deleteDish(title);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public List<FullDishDto> findAllDish() {
        log.info("Request to find all FullDishDto :{}");
        return dishService.getAllDish();
    }

    @GetMapping("/get/{title}")
    public DishRequestDto getDishByTitle(@PathVariable String title) {
        log.info("Request to get a DishRequestDto by the title :{}", title);
        return dishService.getDishByTitle(title);
    }

    @PutMapping(value = "/{title}")
    public void updateDish(@Valid @RequestBody DishRequestDto dishRequestDto, @PathVariable String title) {
        log.info("Request to update a Dish with a title:{}", title);
        dishService.updateDish(dishRequestDto, title);
    }

    @GetMapping("/sort/by/{sort}/{page}")
    public ResponseEntity<Object> sortBy(@PathVariable String sort, @PathVariable String page) {
        if (sort.equals("price") || sort.equals("title")){
            log.info("sorting all dishes");
            return new ResponseEntity<>(dishService.sortingBy(sort, page), HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Incorrect sorting type (must be price or title)", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/by/{category}/{page}")
    public ResponseEntity<Object> getByCategory(@PathVariable String category, @PathVariable String page) {

        log.info("sorting all dishes by category");
        return new ResponseEntity<>(dishService.getByCategory(category, page), HttpStatus.OK);
    }
}