package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.DishCreateDto;
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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

@RequiredArgsConstructor
@Service
@Log4j2
public class DishServiceImpl implements DishService {

    @Resource
    private final ModelMapper mapper;

    private final DishRepository dishRepository;

    @Override
    public Optional<FullDishDto> createDish(DishCreateDto dishCreateDto) {
        log.info("createDish with a title {}", dishCreateDto.getTitle());
        dishRepository.save(mapper.map(dishCreateDto, Dish.class));
        log.info("New Dish is created with a title {}", dishCreateDto.getTitle());
        return getDishByTitle(dishCreateDto.getTitle());
    }

    @Override
    @Transactional
    public FullDishDto deactivateDish(String title) {
        log.info("deactivateDish with title {}", title);
        FullDishDto dishDto = getDishByTitle(title).orElseThrow();
        Optional<Dish> dish = dishRepository.findById(Long.parseLong(dishDto.getId()));
        dishRepository.deactivateDishByTitle(title);
        dish.orElseThrow().setIsActive(false);
        Dish deactivatedDish = dishRepository.save(dish.orElseThrow());
        log.info("Dish {} is deactivated", dish);
        return mapper.map(deactivatedDish, FullDishDto.class);
    }

    @Override
    @Transactional
    public Optional<FullDishDto> getDishByTitle(String title) {
        log.info("getByTitle {}", title);
        try {
            return Optional.of(mapper.map(dishRepository.findDishByTitle(title), FullDishDto.class));
        } catch (IllegalArgumentException ex) {
            log.info("Dish with title {} wasn't found! ", title);
            throw new NoSuchElementException();
        }
    }

    @Override
    @Transactional
    public Optional<FullDishDto> getDishById(String id) {
        log.info("getDishById {}", id);
        try {
            return Optional.of(mapper.map(dishRepository.findDishById(id), FullDishDto.class));
        } catch (IllegalArgumentException ex) {
            log.info("Dish with id {} wasn't found! ", id);
            throw new NoSuchElementException();
        }
    }

    @Override
    @Transactional
    public FullDishDto updateDishTitle(String id, String title) {
        log.info("updateDish by id with the title {}", title);
        getDishById(id).orElseThrow();

        Dish dish = dishRepository.findDishById(id);
        dishRepository.updateTitle(id, title);
        Dish updatedDish = dishRepository.save(dish);
        log.info("Dish is updated successfully");
        return mapper.map(updatedDish, FullDishDto.class);
    }

    @Override
    @Transactional
    public DishRequestDto updateDish(DishRequestDto dishRequestDto, String title) {
        FullDishDto fullDto = getDishByTitle(title).orElseThrow();
        log.info("updateDish by title : {}", title);
        Optional<Dish> dish = dishRepository.findById(Long.valueOf(fullDto.getId()));

        String newTitle = dishRequestDto.getTitle() == null ? title : dishRequestDto.getTitle();
        String price = (dishRequestDto.getPrice() == null ? fullDto.getPrice() : dishRequestDto.getPrice());
        String weight = (dishRequestDto.getWeight() == null ? fullDto.getWeight() : dishRequestDto.getWeight());
        dish.orElseThrow().setTitle(newTitle);
        dish.orElseThrow().setPrice(Integer.valueOf(price));
        dish.orElseThrow().setWeight(Integer.valueOf(weight));

        Dish updatedDish = dishRepository.save(dish.orElseThrow());
        log.info("Dish is updated successfully");
        return mapper.map(updatedDish, DishRequestDto.class);
    }

    @Override
    @Transactional
    public List<FullDishDto> getAllDish() {
        log.info("getAllDish method");
        return dishRepository.findAll().stream()
                .map(e -> mapper.map(e, FullDishDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<FullDishDto> sortingBy(String sortingOption, String page) {
        Pageable pagesWithThreeElements = PageRequest.of(parseInt(page), 3, Sort.by(sortingOption));
        log.info("Start method getAllDish() in dish service");
        return dishRepository.findAll(pagesWithThreeElements)
                .stream()
                .map(e -> mapper.map(e, FullDishDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<FullDishDto> getByCategory(String category, String page) {
        Pageable pagesWithThreeElements = PageRequest.of(parseInt(page), 3);
        log.info("Start method getByCategory() in dish service {}", category);
        return dishRepository.findByCategory(pagesWithThreeElements, Categories.valueOf(category))
                .stream()
                .map(e -> mapper.map(e, FullDishDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isStatusActive(String title) {
        log.info("Checking if dish with such title {} is active", title);
        return Boolean.parseBoolean(getDishByTitle(title).orElseThrow().getIsActive());
    }
}
