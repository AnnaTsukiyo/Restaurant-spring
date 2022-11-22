package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FoodRequestDto;
import com.epam.zelener.restaurant.dtos.FullFoodDto;
import com.epam.zelener.restaurant.model.Food;
import com.epam.zelener.restaurant.repositories.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

    @Resource
    private ModelMapper mapper;

    private final FoodRepository foodRepository;

    @Override
    @Transactional
    public void createFood(FoodRequestDto foodRequestDto) {
        Food food = mapper.map(foodRequestDto, Food.class);
        log.info("New food is created with a title {}", foodRequestDto.getTitle());
        foodRepository.save(food);
    }

    @Override
    @Transactional
    public void deleteFood(String title) {
        log.info("deleteFood with title {}", title);
        Food food = mapper.map(foodRepository.findFoodByTitle(title), Food.class);
        foodRepository.save(food);
    }

    @Override
    @Transactional
    public FoodRequestDto getFoodByTitle(String title) {
        log.info("getFoodByTitle {}", title);
        return mapper.map(foodRepository.findFoodByTitle(title), FoodRequestDto.class);
    }

    @Override
    public void updateFood(FoodRequestDto foodRequestDto, String title) {
        Food food = mapper.map(foodRequestDto, Food.class);
        log.info("updateFood by title {}", title);
        foodRepository.save(food);
    }


    @Override
    @Transactional
    public void updateFoodTitle(long id, String title) {
        log.info("updateFoodTitle with the title {}", title);
        foodRepository.updateTitle(id, title);
    }

    @Override
    @Transactional
    public List<FullFoodDto> getAllFood() {
        log.info("getAllFood method");
        return foodRepository.findAll().stream()
                .map(e -> mapper.map(e, FullFoodDto.class))
                .collect(Collectors.toList());
    }
}
