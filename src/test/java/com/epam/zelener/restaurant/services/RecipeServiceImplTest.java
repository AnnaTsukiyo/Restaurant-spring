package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullRecipeDto;
import com.epam.zelener.restaurant.dtos.RecipeCreateDto;
import com.epam.zelener.restaurant.dtos.RecipeRequestDto;
import com.epam.zelener.restaurant.model.Food;
import com.epam.zelener.restaurant.model.Ingredient;
import com.epam.zelener.restaurant.model.Recipe;
import com.epam.zelener.restaurant.repositories.RecipeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeServiceImpl recipeService;
    private final ModelMapper mapper = new ModelMapper();
    private Recipe recipe;

    @BeforeEach
    void setUp() {
        recipeService = new RecipeServiceImpl(mapper, recipeRepository);

        recipe = new Recipe(1L, 120, "Cooking Sushi California", 30, List.of(
                new Ingredient(1L, List.of(new Food(1L, "Salmon", "Smoked salmon from Norway",
                        true, LocalDate.of(2022, Month.DECEMBER, 1),
                        LocalDate.of(2022, Month.DECEMBER, 10),
                        LocalDateTime.now(), LocalDateTime.now())), true, 20, 3)),true);
    }

    @DisplayName("JUNIT Test DishServiceImpl getDishByTitle() method --positive test case scenario")
    @Test
    void getDishByTitle_positiveTest() {

      Recipe recipe = new Recipe(2L, 10, "Cooking Sushi Philadelphia", 35, List.of(
                new Ingredient(1L, List.of(new Food(1L, "Cream Philadelphia", "Cream Cheese Philadelphia",
                        true, LocalDate.of(2022, Month.DECEMBER, 1),
                        LocalDate.of(2023, Month.DECEMBER, 10),
                        LocalDateTime.now(), LocalDateTime.now())), true, 1, 1)),true);


        FullRecipeDto dto = new FullRecipeDto();
        dto.setTitle("Cooking Sushi Philadelphia");
        lenient().when(recipeRepository.findRecipeByTitle("Cooking Sushi Philadelphia")).thenReturn(recipe);

        Assertions.assertEquals("Cooking Sushi Philadelphia", recipe.getTitle());

        System.out.println("Recipe is found :" + recipeService.getRecipeByTitle("Cooking Sushi Philadelphia"));
        lenient().when(recipeRepository.findRecipeByTitle("Cooking Sushi Philadelphia")).thenReturn(recipe);
        Assertions.assertEquals("Cooking Sushi Philadelphia", recipe.getTitle());
        System.out.printf("Recipe is found :%s%n", recipeService.getRecipeByTitle("Cooking Sushi Philadelphia"));
    }

    @DisplayName("JUNIT Test RecipeServiceImpl getRecipeByTitle() method --negative test case scenario")
    @Test
    void getRecipeByTitle_negativeTest() {
        lenient().when(recipeRepository.findRecipeByTitle("Cooking Sushi Philadelphia")).thenThrow(NoSuchElementException.class);
        Assertions.assertThrows(NoSuchElementException.class, () -> recipeService.getRecipeByTitle("Cooking Sushi Philadelphia").orElseThrow());
        System.out.println("Recipe is not found with title{} :" + "Cooking Sushi Philadelphia");
    }

    @DisplayName("JUNIT Test RecipeServiceImpl getAllRecipe() method --- positive test case scenario ")
    @Test
    void getAllRecipe_positiveTest() {
        lenient().when(recipeRepository.findAll()).thenReturn(List.of(recipe));
        List<FullRecipeDto> list = recipeService.getAllRecipe();
        Assertions.assertFalse(list.isEmpty());
        assertThat(list.get(0).getTitle()).isEqualTo(recipe.getTitle());
    }

    @DisplayName("JUNIT Test RecipeServiceImpl updateRecipe() method --positive test case scenario")
    @Test
    void updateRecipe_positiveTest() {
        lenient().when(recipeRepository.findById(recipe.getId())).thenReturn(Optional.of(recipe));
        lenient().when(recipeRepository.save(recipe)).thenReturn(recipe);
        lenient().when(recipeRepository.findRecipeByTitle(recipe.getTitle())).thenReturn(recipe);

        RecipeRequestDto recipeRequestDto = new RecipeRequestDto();
        recipeRequestDto.setDuration(String.valueOf(45));
        recipeRequestDto.setTitle("Cooking Sushi Philadelphia+");
        RecipeRequestDto requestDto = recipeService.updateRecipe(recipeRequestDto, recipe.getTitle());

        assertThat(requestDto.getTitle()).isEqualTo("Cooking Sushi Philadelphia+");
        assertThat(requestDto.getDuration()).isEqualTo("45");
    }

    @DisplayName("JUNIT Test RecipeServiceImpl createRecipe() method --- positive test case scenario ")
    @Test
    void createRecipe_positiveTest() {

        RecipeCreateDto recipeCreateDto = new RecipeCreateDto("1", "111","Cooking Sushi California", "60");
        Recipe recipe1 = mapper.map(recipeCreateDto, Recipe.class);
        lenient().when(recipeRepository.save(recipe1)).thenReturn(recipe1);
        lenient().when(recipeRepository.findRecipeByTitle("Cooking Sushi California")).thenReturn(recipe1);

        Optional<FullRecipeDto> createdRecipe = recipeService.createRecipe(recipeCreateDto);
        assertThat(createdRecipe.orElseThrow().getTitle()).isEqualTo("Cooking Sushi California");
    }
    @DisplayName("JUNIT Test RecipeServiceImpl deactivateRecipe() --positive test case scenario")
    @Test
    void deactivateRecipe_positiveTest() {
        lenient().when(recipeRepository.findById(( recipe.getId()))).thenReturn(Optional.of(recipe));
        lenient().when(recipeRepository.save(recipe)).thenReturn(recipe);
        lenient().when(recipeRepository.findRecipeByTitle(recipe.getTitle())).thenReturn(recipe);

        FullRecipeDto fullRecipeDto = recipeService.deactivateRecipe(recipe.getTitle());
        assertThat(fullRecipeDto.getIsActive().equals("false"));

    }
    @DisplayName("JUNIT Test DishServiceImpl isStatusActive() method --positive test case scenario")
    @Test
    void testIsStatusActive() {
        when(recipeRepository.findRecipeByTitle("Cooking Sushi California")).thenReturn(recipe);
        assertThat(recipeService.isStatusActive("Cooking Sushi California")).isTrue();
    }

}
