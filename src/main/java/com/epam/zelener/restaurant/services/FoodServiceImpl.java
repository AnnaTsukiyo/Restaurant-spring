package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FoodCreateDto;
import com.epam.zelener.restaurant.dtos.FoodRequestDto;
import com.epam.zelener.restaurant.dtos.FullFoodDto;
import com.epam.zelener.restaurant.model.Food;
import com.epam.zelener.restaurant.model.Status;
import com.epam.zelener.restaurant.repositories.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

    @Resource
    private final ModelMapper mapper;

    private final FoodRepository foodRepository;

    @Override
    @Transactional
    public Optional<FullFoodDto> createFood(FoodCreateDto foodRequestDto) {
        log.info("New food is created with a title {}", foodRequestDto.getTitle());
        foodRepository.save(mapper.map(foodRequestDto, Food.class));
        return getFoodByTitle(foodRequestDto.getTitle());
    }

    @Override
    @Transactional
    public FullFoodDto deactivateFood(String title) {

        log.info("deactivateFood with title {}", title);
        FullFoodDto foodDto = getFoodByTitle(title).orElseThrow();
        Optional<Food> food = foodRepository.findById(Long.parseLong(foodDto.getId()));
        foodRepository.updateStatus(title);
        food.orElseThrow().setIsActive(false);
        food.orElseThrow().setStatus(Status.INACTIVE);
        Food deactivatedFood = foodRepository.save(food.orElseThrow());
        log.info("Food {} is deactivated", food);
        return mapper.map(deactivatedFood, FullFoodDto.class);
    }

    @Override
    @Transactional
    public Optional<FullFoodDto> getFoodByTitle(String title) {
        log.info("getFoodByTitle {}", title);
        try {
            return Optional.of(mapper.map(foodRepository.findFoodByTitle(title), FullFoodDto.class));
        } catch (IllegalArgumentException ex) {
            log.info("Food with title {} wasn't found! ", title);
            throw new NoSuchElementException();
        }
    }

    @Override
    public FoodRequestDto updateFood(FoodRequestDto foodRequestDto, String title) {
        log.info("updateFood by title {}", title);
        getFoodByTitle(title).orElseThrow();
        Food food = foodRepository.findFoodByTitle(title);

        if (food.getTitle().equals(title)) {
            if (foodRequestDto.getDescription() != null) {
                food.setDescription((foodRequestDto.getDescription()));
            }

            Food updatedFood = foodRepository.save(food);
            return mapper.map(updatedFood, FoodRequestDto.class);
        }
        log.info("Food is updated successfully");
        return null;
    }

    @Override
    @Transactional
    public FullFoodDto updateFoodTitle(String id, String title) {
        log.info("updateFoodTitle by id with the title {}", title);
        getFoodById(id).orElseThrow();

        Food food = foodRepository.findFoodById(id);
        foodRepository.updateTitle(id, title);
        Food updatedFood = foodRepository.save(food);
        log.info("Food is updated successfully");
        return mapper.map(updatedFood, FullFoodDto.class);

    }

    @Override
    @Transactional
    public List<FullFoodDto> getAllFood() {
        log.info("getAllFood method");
        return foodRepository.findAll().stream()
                .map(e -> mapper.map(e, FullFoodDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<FullFoodDto> getFoodById(String id) {
        log.info("getFoodById {}", id);
        try {
            return Optional.of(mapper.map(foodRepository.findFoodById(id), FullFoodDto.class));
        } catch (IllegalArgumentException ex) {
            log.info("Food with id {} wasn't found! ", id);
            throw new NoSuchElementException();
        }
    }

    @Override
    public boolean isStatusActive(String title) {
        log.info("Checking if food with such title {} is active", title);
        return Boolean.parseBoolean(getFoodByTitle(title).orElseThrow().getIsActive());
    }
}
