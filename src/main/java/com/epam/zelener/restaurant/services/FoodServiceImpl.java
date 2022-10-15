package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FoodRequestDto;
import com.epam.zelener.restaurant.model.Food;
import com.epam.zelener.restaurant.repositories.FoodRepository;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {

    @Resource
    private ModelMapper mapper;
    @Resource
    private FoodRepository foodRepository;

    @Override
    public void createFood(FoodRequestDto foodRequestDto) {
        Food food = mapper.map(foodRequestDto, Food.class);
        System.out.println(food);
        foodRepository.save(food);
    }
}
