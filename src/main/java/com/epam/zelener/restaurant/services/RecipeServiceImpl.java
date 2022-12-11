package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullRecipeDto;
import com.epam.zelener.restaurant.dtos.RecipeCreateDto;
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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    @Resource
    private final ModelMapper mapper;

    private final RecipeRepository recipeRepository;

    @Override
    @Transactional
    public Optional<FullRecipeDto> createRecipe(RecipeCreateDto recipeCreateDto) {
        log.info("createRecipe with title {}", recipeCreateDto.getTitle());
        Recipe recipe = mapper.map(recipeCreateDto, Recipe.class);
        log.info("New recipe is created with a title {}", recipeCreateDto.getTitle());
        return getRecipeByTitle(recipeCreateDto.getTitle());
    }

    @Override
    @Transactional
    public FullRecipeDto deactivateRecipe(String title) {
        log.info("deleteRecipe by title {}", title);
        FullRecipeDto recipeDto = getRecipeByTitle(title).orElseThrow();
        Optional<Recipe> recipe = recipeRepository.findById(Long.valueOf(recipeDto.getId()));
        recipeRepository.deactivateRecipeByTitle(title);
        recipe.orElseThrow().setIsActive(false);
        Recipe deactivatedRecipe = recipeRepository.save(recipe.orElseThrow());
        log.info("Recipe {} is deactivated", title);
        return mapper.map(deactivatedRecipe, FullRecipeDto.class);
    }

    @Override
    @Transactional
    public Optional<FullRecipeDto> getRecipeByTitle(String title) {
        log.info("getRecipeByTitle {}", title);
        try {
            return Optional.of(mapper.map(recipeRepository.findRecipeByTitle(title), FullRecipeDto.class));
        } catch (IllegalArgumentException ex) {
            log.info("Recipe with title {} wasn't found! ", title);
            throw new NoSuchElementException();
        }
    }

    @Override
    @Transactional
    public RecipeRequestDto updateRecipe(RecipeRequestDto recipeRequestDto, String title) {
        FullRecipeDto fullRecipeDto = getRecipeByTitle(title).orElseThrow();
        log.info("updateRecipe by title : {}", title);
        Optional<Recipe> recipe = recipeRepository.findById(Long.valueOf(fullRecipeDto.getId()));

        String newTitle = recipeRequestDto.getTitle() == null ? title : recipeRequestDto.getTitle();
        String duration = (recipeRequestDto.getDuration() == null ? fullRecipeDto.getDuration() : recipeRequestDto.getDuration());
        String number = (recipeRequestDto.getNumber() == null ? fullRecipeDto.getNumber() : recipeRequestDto.getNumber());

        recipe.orElseThrow().setTitle(newTitle);
        recipe.orElseThrow().setNumber(Integer.parseInt(number));
        recipe.orElseThrow().setDuration(Integer.parseInt(duration));

        Recipe updatedRecipe = recipeRepository.save(recipe.orElseThrow());
        log.info("Recipe is updated successfully");
        return mapper.map(updatedRecipe, RecipeRequestDto.class);
    }

    @Override
    @Transactional
    public FullRecipeDto updateRecipeTitle(String id, String title) {
        log.info("updateRecipe title {}", title);
        getRecipeById(id).orElseThrow();

        Recipe recipe = recipeRepository.getById(Long.valueOf(id));
        recipeRepository.updateTitle(id, title);
        Recipe updatedDish = recipeRepository.save(recipe);
        log.info("Recipe is updated successfully");
        return mapper.map(updatedDish, FullRecipeDto.class);
    }

    @Override
    @Transactional
    public Optional<FullRecipeDto> getRecipeById(String id) {
        log.info("getRecipeById {}", id);
        try {
            return Optional.of(mapper.map(recipeRepository.findById(Long.valueOf(id)), FullRecipeDto.class));
        } catch (IllegalArgumentException ex) {
            log.info("Recipe with id {} wasn't found! ", id);
            throw new NoSuchElementException();
        }
    }

    @Override
    @Transactional
    public List<FullRecipeDto> getAllRecipe() {
        log.info("getAllRecipe method");
        return recipeRepository.findAll().stream()
                .map(e -> mapper.map(e, FullRecipeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isStatusActive(String title) {
        log.info("Checking if recipe with such title {} is active", title);
        return Boolean.parseBoolean(String.valueOf(getRecipeByTitle(title).orElseThrow().getIsActive().equals("true")));
    }
}
