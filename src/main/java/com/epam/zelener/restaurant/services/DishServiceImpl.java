package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.DishRequestDto;
import com.epam.zelener.restaurant.dtos.FullDishDto;
import com.epam.zelener.restaurant.model.Categories;
import com.epam.zelener.restaurant.model.Dish;
import com.epam.zelener.restaurant.repositories.DishRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
public class DishServiceImpl implements DishService {

    @Resource
    private ModelMapper mapper;

    private final DishRepository dishRepository;

    @Override
    public void createDish(DishRequestDto dishRequestDto) {
        log.info("createDish with a title  {}", dishRequestDto.getTitle());
        Dish dish = mapper.map(dishRequestDto, Dish.class);
        dishRepository.save(dish);
    }

    @Override
    public void deleteDish(String title) {
        log.info("deleteDish with title {}", title);
        Dish dish = mapper.map(dishRepository.findDishByTitle(title), Dish.class);
        dishRepository.save(dish);
    }

    @Override
    @Transactional
    public DishRequestDto getDishByTitle(String title) {
        log.info("getByTitle  {}", title);
        return mapper.map(dishRepository.findDishByTitle(title), DishRequestDto.class);
    }

    @Override
    public void updateDish(DishRequestDto dishRequestDto, String title) {
        Dish dish = mapper.map(dishRequestDto, Dish.class);
        log.info("updateDish with title {}", title);
        dishRepository.save(dish);
    }

    @Override
    @Transactional
    public List<FullDishDto> getAllDish() {
        log.info("getAllDish");
        return dishRepository.findAll().stream()
                .map(e -> mapper.map(e, FullDishDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<FullDishDto> sortingBy(String sortingOption, String page) {
        Pageable pagesWithThreeElements = PageRequest.of(Integer.parseInt(page), 3, Sort.by(sortingOption));
        log.info("Start method getAllDish() in dish service");
        return dishRepository.findAll(pagesWithThreeElements)
                .stream()
                .map(e -> mapper.map(e, FullDishDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<FullDishDto> getByCategory(String category, String page) {
        Pageable pagesWithThreeElements = PageRequest.of(Integer.parseInt(page), 3);
        log.info("Start method getByCategory() in dish service {}", category);
        return dishRepository.findByCategory(pagesWithThreeElements, Categories.valueOf(category))
                .stream()
                .map(e -> mapper.map(e, FullDishDto.class))
                .collect(Collectors.toList());
    }
}
