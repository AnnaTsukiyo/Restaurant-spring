package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.IngredientRequestDto;
import com.epam.zelener.restaurant.model.Ingredient;
import com.epam.zelener.restaurant.repositories.IngredientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class IngredientServiceImpl implements IngredientService {

    @Resource
    private ModelMapper mapper;

    private final IngredientRepository ingredientRepository;

    @Override
    public void createIngredient(IngredientRequestDto ingredientRequestDto) {
        Ingredient ingredient = mapper.map(ingredientRequestDto, Ingredient.class);
        log.info("New ingredient is created with ID {}", ingredientRequestDto.getId());
        ingredientRepository.save(ingredient);
    }

    @Override
    public void deleteIngredient(String id) {
        log.info("deleteIngredient with id {}", id);
Ingredient ingredient = mapper.map(ingredientRepository.findIngredientById(id),Ingredient.class);
        ingredientRepository.save(ingredient);
    }

    @Override
    @Transactional
    public IngredientRequestDto getIngredientById(String id) {
        log.info("getIngredientById {}",id);
        return mapper.map(ingredientRepository.findIngredientById(id), IngredientRequestDto.class);
    }

    @Override
    public void updateIngredient(IngredientRequestDto ingredientRequestDto, String id) {
        Ingredient ingredient = mapper.map(ingredientRepository.findIngredientById(id),Ingredient.class);
        log.info("updateIngredient by id {}",id);
        ingredientRepository.save(ingredient);
    }

    @Override
    @Transactional
    public List<IngredientRequestDto> getAllIngredients() {
        log.info("getAllIngredients ");
        return ingredientRepository.findAll().stream()
                .map(e -> mapper.map(e, IngredientRequestDto.class))
                .collect(Collectors.toList());
    }
}
