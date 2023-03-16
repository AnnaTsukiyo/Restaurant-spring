package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FoodCreateDto;
import com.epam.zelener.restaurant.dtos.FoodRequestDto;
import com.epam.zelener.restaurant.dtos.FullFoodDto;
import com.epam.zelener.restaurant.model.Food;
import com.epam.zelener.restaurant.model.Status;
import com.epam.zelener.restaurant.repositories.FoodRepository;
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
class FoodServiceImplTest {

    @Mock
    private FoodRepository foodRepository;

    @InjectMocks
    private FoodServiceImpl foodService;
    private final ModelMapper mapper = new ModelMapper();
    private Food food;

    @BeforeEach
    void setUp() {
        foodService = new FoodServiceImpl(mapper, foodRepository);
        food = new Food(1L, "Coffee", "Coffee black Arabica 5+", true,  Status.ACTIVE, LocalDate.of(2022, Month.DECEMBER, 1),
                LocalDate.of(2023, Month.APRIL, 10),
                LocalDateTime.now(), LocalDateTime.now());
    }

    @DisplayName("JUNIT Test FoodServiceImpl createFood() --positive test case scenario")
    @Test
    void createFood_positiveTest() {
        FoodCreateDto foodRequestDto = new FoodCreateDto("Milk", "Cow Milk Bio", "2022-11-10", "2022-12-29");

        Food food = mapper.map(foodRequestDto, Food.class);
        lenient().when(foodRepository.save(food)).thenReturn(food);
        lenient().when(foodRepository.findFoodByTitle("Milk")).thenReturn(food);

        Optional<FullFoodDto> createdFood = foodService.createFood(foodRequestDto);
        assertThat(createdFood.orElseThrow().getTitle()).isEqualTo("Milk");

    }

    @DisplayName("JUNIT Test FoodServiceImpl deactivateFood() --positive test case scenario")
    @Test
    void deactivateFood_positiveTest() {
        lenient().when(foodRepository.findById((food.getId()))).thenReturn(Optional.of(food));
        lenient().when(foodRepository.save(food)).thenReturn(food);
        lenient().when(foodRepository.findFoodByTitle(food.getTitle())).thenReturn(food);

        FullFoodDto foodDto = foodService.deactivateFood(food.getTitle());
        assertThat(foodDto.getIsActive().equals("false"));

    }

    @DisplayName("JUNIT Test ManagerServiceImpl getFoodById() method --positive test case scenario")
    @Test
    void getFoodById_positiveTest() {
        Food foodNew = new Food(4L, "Coffee R", "Coffee Robusta 10", true, Status.ACTIVE, LocalDate.of(2022, Month.DECEMBER, 1),
                LocalDate.of(2023, Month.APRIL, 10),
                LocalDateTime.now(), LocalDateTime.now());

        FullFoodDto dto = new FullFoodDto();
        dto.setId("4");
        lenient().when(foodRepository.findFoodById("4")).thenReturn(foodNew);

        Assertions.assertEquals(foodNew.getId(), 4);
        System.out.println("Food is found :" + foodService.getFoodById("4"));

    }

    @DisplayName("JUNIT Test FoodServiceImpl getFoodByTitle() --positive test case scenario")
    @Test
    void getFoodByTitle_positiveTest() {

        Food food = new Food(2L, "Black Tea with Bergamot", "Black Tea Ahmad with Bergamot ", true, Status.ACTIVE, LocalDate.of(2022, Month.NOVEMBER, 1),
                LocalDate.of(2023, Month.NOVEMBER, 1),
                LocalDateTime.now(), LocalDateTime.now());
        FullFoodDto dto = new FullFoodDto();
        dto.setTitle("Black Tea with Bergamot");
        lenient().when(foodRepository.findFoodByTitle("Black Tea with Bergamot")).thenReturn(food);

        Assertions.assertEquals("Black Tea with Bergamot", food.getTitle());

        System.out.println("Food is found :" + foodService.getFoodByTitle("Black Tea with Bergamot"));
        lenient().when(foodRepository.findFoodByTitle("Black Tea with Bergamot")).thenReturn(food);
        Assertions.assertEquals(food.getTitle(), "Black Tea with Bergamot");
        System.out.printf("Food is found :%s%n", foodService.getFoodByTitle("Black Tea with Bergamot"));

    }

    @DisplayName("JUNIT Test ManagerServiceImpl getFoodByTitle() method --negative test case scenario")
    @Test
    void getFoodByTitle_negativeTest() {
        lenient().when(foodRepository.findFoodByTitle("Coffee")).thenThrow(NoSuchElementException.class);
        Assertions.assertThrows(NoSuchElementException.class, () -> foodService.getFoodByTitle("Sushi California").orElseThrow(NoSuchElementException::new));
        System.out.println("Food is not found with title{} :" + "Coffee");
    }

    @DisplayName("JUNIT Test ManagerServiceImpl updateFood() method --negative test case scenario")
    @Test
    void updateFood_positiveTest() {

        when(foodRepository.findFoodByTitle(food.getTitle())).thenReturn(food);
        when(foodRepository.save(food)).thenReturn(food);

        FoodRequestDto editFoodDto = new FoodRequestDto();
        editFoodDto.setDescription("Cow Milk Bio 1.5% fet");
        FoodRequestDto updateFood = foodService.updateFood(editFoodDto, food.getTitle());
        FoodRequestDto foodRequestDto = mapper.map(food, FoodRequestDto.class);

        assertThat(foodRequestDto.getDescription()).isEqualTo("Cow Milk Bio 1.5% fet");
    }

    @DisplayName("JUNIT Test ManagerServiceImpl updateFoodTitle() method --positive test case scenario")
    @Test
    void updateFoodTitle_positiveTest() {

        lenient().when(foodRepository.findFoodById(String.valueOf((food.getId())))).thenReturn(food);
        lenient().when(foodRepository.save(food)).thenReturn(food);
        lenient().when(foodRepository.findFoodByTitle(food.getTitle())).thenReturn(food);

        FullFoodDto dto = new FullFoodDto();
        dto.setId(String.valueOf(food.getId()));
        dto.setTitle("Coffee Black");
        foodService.updateFoodTitle(dto.getId(), dto.getTitle());
        assertThat(dto.getTitle()).isEqualTo("Coffee Black");
    }

    @DisplayName("JUNIT Test FoodServiceImpl getAllFood() --positive test case scenario")
    @Test
    void getAllFood_positiveTest() {
        lenient().when(foodRepository.findAll()).thenReturn(List.of(food));
        List<FullFoodDto> list = foodService.getAllFood();
        Assertions.assertFalse(list.isEmpty());
        assertThat(list.get(0).getTitle()).isEqualTo(food.getTitle());
    }
    @DisplayName("JUNIT Test FoodServiceImpl isStatusActive() method --positive test case scenario")
    @Test
    void testIsStatusActive() {
        when(foodRepository.findFoodByTitle("Coffee")).thenReturn(food);
        assertThat(foodService.isStatusActive("Coffee")).isTrue();
    }
}
