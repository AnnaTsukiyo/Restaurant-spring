package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullIngredientDto;
import com.epam.zelener.restaurant.dtos.IngredientCreateDto;
import com.epam.zelener.restaurant.dtos.IngredientRequestDto;
import com.epam.zelener.restaurant.model.Food;
import com.epam.zelener.restaurant.model.Ingredient;
import com.epam.zelener.restaurant.repositories.IngredientRepository;
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
class IngredientServiceImplTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private IngredientServiceImpl ingredientService;
    private final ModelMapper mapper = new ModelMapper();
    private Ingredient ingredient;

    @BeforeEach
    void setUp() {
        ingredientService = new IngredientServiceImpl(mapper, ingredientRepository);
        ingredient = new Ingredient(1L,List.of( new Food(1L, "Black Olives", "Pitted Greek black olives ",
                true, LocalDate.of(2022, Month.OCTOBER, 1),
                LocalDate.of(2023, Month.DECEMBER, 10),
                LocalDateTime.now(), LocalDateTime.now())), true, 30, 30);
    }

    @DisplayName("JUNIT Test IngredientServiceImpl createIngredient() --positive test case scenario")
    @Test
    void createIngredient_positiveTest() {

        IngredientCreateDto ingredientCreateDto = new IngredientCreateDto("2", "10", "20", "ACTIVE");
        Ingredient ingredientNew = mapper.map(ingredientCreateDto, Ingredient.class);
        lenient().when(ingredientRepository.save(ingredientNew)).thenReturn(ingredientNew);
        lenient().when(ingredientRepository.findIngredientById(ingredientCreateDto.getId())).thenReturn(ingredientNew);

        Optional<FullIngredientDto> createdIngredient = ingredientService.createIngredient(ingredientCreateDto);
        assertThat(createdIngredient.orElseThrow().getId()).isEqualTo(String.valueOf(2L));

    }

    @DisplayName("JUNIT Test DishServiceImpl deactivateDish() --positive test case scenario")
    @Test
    void deactivateIngredient_positiveTest() {

        lenient().when(ingredientRepository.findById(Math.toIntExact(ingredient.getId()))).thenReturn(Optional.of(ingredient));
        lenient().when(ingredientRepository.save(ingredient)).thenReturn(ingredient);
        lenient().when(ingredientRepository.findIngredientById(String.valueOf(ingredient.getId()))).thenReturn(ingredient);

        FullIngredientDto ingredientDto = ingredientService.deactivateIngredient(String.valueOf(ingredient.getId()));
        assertThat(ingredientDto.getIsActive().equals("false"));

    }

    @DisplayName("JUNIT Test IngredientServiceImpl getIngredientById() --positive test case scenario")
    @Test
    void getIngredientById_positiveTest() {

        Ingredient ingredientNew = new Ingredient(3L,List.of( new Food(1L, "Philadelphia cream cheese", "Philadelphia cream cheese Original 200g",
                true, LocalDate.of(2022, Month.OCTOBER, 15),
                LocalDate.of(2023, Month.OCTOBER, 15),
                LocalDateTime.now(), LocalDateTime.now())), true, 5, 1);

        FullIngredientDto dto = new FullIngredientDto();
        dto.setId("3");
        System.out.println(ingredientNew.getId());
        lenient().when(ingredientRepository.findIngredientById("3")).thenReturn(ingredientNew);

        Assertions.assertEquals(3,ingredientNew.getId());
        System.out.println("Ingredient is found :" + ingredientService.getIngredientById("3"));
    }

    @DisplayName("JUNIT Test IngredientServiceImpl getIngredientById() method --negative test case scenario")
    @Test
    void getIngredientById_negativeTest() {
        lenient().when(ingredientRepository.getById(1)).thenThrow(NoSuchElementException.class);
        Assertions.assertThrows(NoSuchElementException.class, () -> ingredientService.getIngredientById("1").orElseThrow(NoSuchElementException::new));
        System.out.println("Ingredient is not found with id{} :" + "1");
    }

    @DisplayName("JUNIT Test IngredientServiceImpl updateIngredient() method --positive test case scenario")
    @Test
    void updateIngredient_positiveTest() {
        when(ingredientRepository.findIngredientById(String.valueOf(ingredient.getId()))).thenReturn(ingredient);
        when(ingredientRepository.findById(Math.toIntExact((ingredient.getId())))).thenReturn(Optional.of(ingredient));
        when(ingredientRepository.save(ingredient)).thenReturn(ingredient);

        IngredientRequestDto ingredientRequestDto = mapper.map(ingredient, IngredientRequestDto.class);
        ingredientRequestDto.setUnits("10");
        ingredientRequestDto.setQuantity("10");
        IngredientRequestDto updateIngredient = ingredientService.updateIngredient(ingredientRequestDto, String.valueOf(ingredient.getId()));
        assertThat(updateIngredient.getUnits()).isEqualTo("10");
        assertThat(updateIngredient.getQuantity()).isEqualTo("10");
    }

    @DisplayName("JUNIT Test IngredientServiceImpl updateIngredientQuantity() method --positive test case scenario")
    @Test
    void updateIngredientQuantity_positiveTest() {

        when(ingredientRepository.findIngredientById("1")).thenReturn(ingredient);
        when(ingredientRepository.findById(Math.toIntExact((ingredient.getId())))).thenReturn(Optional.of(ingredient));
        when(ingredientRepository.save(ingredient)).thenReturn(ingredient);

        IngredientRequestDto ingredientRequestDto = mapper.map(ingredient, IngredientRequestDto.class);
        ingredientRequestDto.setQuantity("10");
        IngredientRequestDto updateIngredient = ingredientService.updateIngredient(ingredientRequestDto, String.valueOf(ingredient.getId()));
        assertThat(updateIngredient.getQuantity()).isEqualTo("10");
    }

    @DisplayName("JUNIT Test IngredientServiceImpl getAllIngredients() method --positive test case scenario")
    @Test
    void getAllIngredients_positiveTest() {
        lenient().when(ingredientRepository.findAll()).thenReturn(List.of(ingredient));
        List<FullIngredientDto> list = ingredientService.getAllIngredients();
        Assertions.assertFalse(list.isEmpty());
        assertThat(list.get(0).getFoodList()).isEqualTo(ingredient.getFoodList());
    }
}
