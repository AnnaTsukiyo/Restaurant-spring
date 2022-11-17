package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullRecipeDto;
import com.epam.zelener.restaurant.dtos.RecipeRequestDto;
import com.epam.zelener.restaurant.model.Recipe;
import com.epam.zelener.restaurant.repositories.RecipeRepository;
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
public class RecipeServiceImpl implements RecipeService {

    @Resource
    private ModelMapper mapper;

    private final RecipeRepository recipeRepository;

    @Override
    public void createRecipe(RecipeRequestDto recipeRequestDto) {
        Recipe recipe = mapper.map(recipeRequestDto, Recipe.class);
        log.info("New recipe is created with a title {}", recipeRequestDto.getTitle());
        recipeRepository.save(recipe);
    }

    @Override
    public void deleteRecipe(String title) {
        log.info("deleteRecipe by title {}", title);
        Recipe recipe = mapper.map(recipeRepository.findRecipeByTitle(title), Recipe.class);
        recipeRepository.save(recipe);
    }

    @Override
    public RecipeRequestDto getRecipeByTitle(String title) {
        log.info("getRecipeByTitle {}", title);
        return mapper.map(recipeRepository.findRecipeByTitle(title), RecipeRequestDto.class);
    }

    @Override
    public void updateRecipe(RecipeRequestDto recipeRequestDto, String title) {
        Recipe recipe = mapper.map(recipeRequestDto, Recipe.class);
        log.info("updateRecipe by title {}", title);
        recipeRepository.save(recipe);

    }

    @Override
    @Transactional
    public List<FullRecipeDto> getAllRecipe() {
        log.info("getAllRecipe ");
        return recipeRepository.findAll().stream()
                .map(e -> mapper.map(e, FullRecipeDto.class))
                .collect(Collectors.toList());
    }
}
