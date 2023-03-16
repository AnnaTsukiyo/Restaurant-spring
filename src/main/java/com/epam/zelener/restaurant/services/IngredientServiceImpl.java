package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullIngredientDto;
import com.epam.zelener.restaurant.dtos.IngredientCreateDto;
import com.epam.zelener.restaurant.dtos.IngredientRequestDto;
import com.epam.zelener.restaurant.model.Ingredient;
import com.epam.zelener.restaurant.repositories.IngredientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class IngredientServiceImpl implements IngredientService {

    @Resource
    private final ModelMapper mapper;

    private final IngredientRepository ingredientRepository;

    @Override
    public Optional<FullIngredientDto> createIngredient(@Valid IngredientCreateDto createDto) {
        log.info("createIngredient with an Id  {}", createDto.getId());
         ingredientRepository.save(mapper.map(createDto, Ingredient.class));
        log.info("New Ingredient is created ");
        return getIngredientById(createDto.getId());
    }
    @Override
    @Transactional
    public FullIngredientDto deactivateIngredient(String id) {
        log.info("deleteIngredient with id {}", id);
        FullIngredientDto requestDto = getIngredientById(id).orElseThrow();
        Optional<Ingredient> ingredient = ingredientRepository.findById(Long.valueOf((requestDto.getId())));
        ingredientRepository.deactivateIngredientById(id);
        ingredient.orElseThrow().setIsActive(false);
        Ingredient deactivatedIngredient = ingredientRepository.save(ingredient.orElseThrow());
        log.info("Ingredient {} is deactivated", ingredient);
        return mapper.map(deactivatedIngredient, FullIngredientDto.class);

    }

    @Override
    @Transactional
    public Optional<FullIngredientDto> getIngredientById(String id) {
        log.info("getIngredientById {}", id);
        try {
            return Optional.of(mapper.map(ingredientRepository.findIngredientById(id), FullIngredientDto.class));
        } catch (IllegalArgumentException ex) {
            log.info("Ingredient with id {} wasn't found! ", id);
            throw new NoSuchElementException();
        }
    }

    @Override
    @Transactional
    public IngredientRequestDto updateIngredient(IngredientRequestDto ingredientRequestDto, String id) {

        FullIngredientDto fullIngredientDto = getIngredientById(id).orElseThrow();
        log.info("updateIngredient by id {}", id);
        Optional<Ingredient> ingredient = ingredientRepository.findById(Long.valueOf((id)));

        String newId = ingredientRequestDto.getId() == null ? id : ingredientRequestDto.getId();
        String units = (ingredientRequestDto.getUnits() == null ? fullIngredientDto.getUnits() : ingredientRequestDto.getUnits());
        String quantity = (ingredientRequestDto.getQuantity() == null ? fullIngredientDto.getQuantity() : ingredientRequestDto.getQuantity());
        ingredient.orElseThrow().setId(Long.valueOf(newId));
        ingredient.orElseThrow().setUnits(Integer.parseInt(units));
        ingredient.orElseThrow().setQuantity(Integer.parseInt(quantity));
        Ingredient updatedIngredient = ingredientRepository.save(ingredient.orElseThrow());

        log.info("Ingredient is updated successfully");
        return mapper.map(updatedIngredient, IngredientRequestDto.class);

    }

    @Override
    public IngredientRequestDto updateIngredientQuantity(String id, String quantity) {
        log.info("updateIngredientQuantity with the quantity {}", quantity);
        getIngredientById(id).orElseThrow();

        Ingredient ingredient = ingredientRepository.getById(Long.valueOf(id));
        ingredientRepository.updateQuantity(id);

        Ingredient updatedIngredient = ingredientRepository.save(ingredient);
        log.info("User is updated successfully");
        return mapper.map(updatedIngredient, IngredientRequestDto.class);
    }

    @Override
    @Transactional
    public List<FullIngredientDto> getAllIngredients() {
        log.info("getAllIngredients method");
        return ingredientRepository.findAll().stream()
                .map(e -> mapper.map(e, FullIngredientDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isStatusActive(String id) {
        log.info("Checking if ingredient with such id {} is active", id);
        return Boolean.parseBoolean(getIngredientById(id).orElseThrow().getIsActive());
    }
}
