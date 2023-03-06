//package com.epam.zelener.restaurant.services;
//
//import com.epam.zelener.restaurant.dtos.DishCreateDto;
//import com.epam.zelener.restaurant.dtos.DishRequestDto;
//import com.epam.zelener.restaurant.dtos.FullDishDto;
//import com.epam.zelener.restaurant.model.Dish;
//import com.epam.zelener.restaurant.model.Food;
//import com.epam.zelener.restaurant.model.Ingredient;
//import com.epam.zelener.restaurant.model.Status;
//import com.epam.zelener.restaurant.repositories.DishRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//import org.springframework.data.domain.*;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.Month;
//import java.util.Comparator;
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import static com.epam.zelener.restaurant.model.Categories.*;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.mockito.Mockito.lenient;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class DishServiceImplTest {
//
//    @Mock
//    private DishRepository dishRepository;
//
//    @InjectMocks
//    private DishServiceImpl dishService;
//    private final ModelMapper mapper = new ModelMapper();
//    private Dish dish;
//
//    @BeforeEach
//    void setUp() {
//        dishService = new DishServiceImpl(mapper, dishRepository);
//        dish = new Dish(1L, 120, "Sushi California", 200, SUSHI, true, Status.ACTIVE,List.of(
//                new Ingredient(1L, List.of(new Food(1L, "Salmon", "Smoked salmon from Norway",
//                        true, Status.ACTIVE, LocalDate.of(2022, Month.DECEMBER, 1),
//                        LocalDate.of(2022, Month.DECEMBER, 10),
//                        LocalDateTime.now(), LocalDateTime.now())), true, 20, 3)));
//    }
//
//    @DisplayName("JUNIT Test DishServiceImpl createDish() --positive test case scenario")
//    @Test
//    void createDish_positiveTest() {
//
//        DishCreateDto createDto = new DishCreateDto("Miso Soup", "75", "300", String.valueOf(SOUP));
//
//        Dish dish = mapper.map(createDto, Dish.class);
//        lenient().when(dishRepository.save(dish)).thenReturn(dish);
//        lenient().when(dishRepository.findDishByTitle("Miso Soup")).thenReturn(dish);
//
//        Optional<FullDishDto> createdDish = dishService.createDish(createDto);
//        assertThat(createdDish.orElseThrow().getTitle()).isEqualTo("Miso Soup");
//    }
//
//    @DisplayName("JUNIT Test DishServiceImpl deactivateDish() --positive test case scenario")
//    @Test
//    void deactivateDish_positiveTest() {
//        lenient().when(dishRepository.findById((dish.getId()))).thenReturn(Optional.of(dish));
//        lenient().when(dishRepository.save(dish)).thenReturn(dish);
//        lenient().when(dishRepository.findDishByTitle(dish.getTitle())).thenReturn(dish);
//
//        FullDishDto dishDto = dishService.deactivateDish(dish.getTitle());
//        assertThat(dishDto.getIsActive().equals("false"));
//
//    }
//
//    @DisplayName("JUNIT Test DishServiceImpl getDishByTitle() method --positive test case scenario")
//    @Test
//    void getDishByTitle_positiveTest() {
//
//        Dish dish = new Dish(5L, 30, "Green tea", 300, DRINKS, true, List.of(
//                new Ingredient(2L, List.of(new Food(2L, "Green tea", "Green tea Ahmad",
//                        true, LocalDate.of(2022, Month.DECEMBER, 1),
//                        LocalDate.of(2023, Month.JANUARY, 25),
//                        LocalDateTime.now(), LocalDateTime.now())), true, 5, 10)));
//        FullDishDto dto = new FullDishDto();
//        dto.setTitle("Green tea");
//        lenient().when(dishRepository.findDishByTitle("Green tea")).thenReturn(dish);
//
//        Assertions.assertEquals("Green tea", dish.getTitle());
//
//        System.out.println("Dish is found :" + dishService.getDishByTitle("Green tea"));
//        lenient().when(dishRepository.findDishByTitle("Green tea")).thenReturn(dish);
//        Assertions.assertEquals(dish.getTitle(), "Green tea");
//        System.out.printf("Dish is found :%s%n", dishService.getDishByTitle("Green tea"));
//    }
//
//
//    @DisplayName("JUNIT Test DishServiceImpl getDishByTitle() method --negative test case scenario")
//    @Test
//    void getDishByTitle_negativeTest() {
//        lenient().when(dishRepository.findDishByTitle("Sushi California")).thenThrow(NoSuchElementException.class);
//        Assertions.assertThrows(NoSuchElementException.class, () -> dishService.getDishByTitle("Sushi California").orElseThrow(NoSuchElementException::new));
//        System.out.println("Dish is not found with title{} :" + "Sushi California");
//    }
//
//    @DisplayName("JUNIT Test DishServiceImpl updateDishTitle() method --positive test case scenario")
//    @Test
//    void updateDishTitle_positiveTest() {
//
//        lenient().when(dishRepository.findDishById(String.valueOf((dish.getId())))).thenReturn(dish);
//        lenient().when(dishRepository.save(dish)).thenReturn(dish);
//        lenient().when(dishRepository.findDishByTitle(dish.getTitle())).thenReturn(dish);
//
//        FullDishDto dishDto = new FullDishDto();
//        dishDto.setId(String.valueOf(dish.getId()));
//        dishDto.setTitle("Sushi Florida");
//        dishService.updateDishTitle(dishDto.getId(), dishDto.getTitle());
//        assertThat(dishDto.getTitle()).isEqualTo("Sushi Florida");
//
//    }
//
//    @DisplayName("JUNIT Test DishServiceImpl updateDish() method --positive test case scenario")
//    @Test
//    void updateDish_positiveTest() {
//        when(dishRepository.findDishByTitle("Sushi California")).thenReturn(dish);
//        when(dishRepository.findById((dish.getId()))).thenReturn(Optional.of(dish));
//        when(dishRepository.save(dish)).thenReturn(dish);
//
//        DishRequestDto dishRequestDto = mapper.map(dish,DishRequestDto.class);
//        dishRequestDto.setPrice("110");
//        dishRequestDto.setWeight("210");
//        DishRequestDto dishDto = dishService.updateDish(dishRequestDto, dish.getTitle());
//
//        assertThat(dishDto.getPrice()).isEqualTo("110");
//        assertThat(dishDto.getWeight()).isEqualTo("210");
//    }
//
//    @DisplayName("JUNIT Test DishServiceImpl getAllDish() method --- positive test case scenario ")
//    @Test
//    void getAllDish_positiveTest() {
//        lenient().when(dishRepository.findAll()).thenReturn(List.of(dish));
//        List<FullDishDto> list = dishService.getAllDish();
//        Assertions.assertFalse(list.isEmpty());
//        assertThat(list.get(0).getTitle()).isEqualTo(dish.getTitle());
//    }
//
//    @Test
//    void sortingBy_positiveTest() {
//        Dish mushroomSoup = new Dish();
//        mushroomSoup.setTitle("Mushroom Soup");
//        mushroomSoup.setPrice(50);
//
//        Dish cheesecake = new Dish();
//        cheesecake.setTitle("Cheesecake");
//        cheesecake.setPrice(33);
//
//        Dish cappuccino = new Dish();
//        cappuccino.setTitle("Cappuccino");
//        cappuccino.setPrice(20);
//
//        Dish caesar = new Dish();
//        caesar.setTitle("Caesar");
//        caesar.setPrice(68);
//
//        //First option: sorting by title
//        Pageable pagesWithThreeElementsSortiedByTitle = PageRequest.of(0, 3, Sort.by("title"));
//        List<Dish> dishesByTitle = Stream.of(mushroomSoup, cheesecake, cappuccino, caesar)
//                .sorted(Comparator.comparing(Dish::getTitle))
//                .collect(Collectors.toList());
//        Page<Dish> pagesPublishersByTitle = new PageImpl<>(dishesByTitle, pagesWithThreeElementsSortiedByTitle, dishesByTitle.size());
//
//        //Second option:  sorting by price
//        Pageable pagesWithThreeElementsSortedByPrice = PageRequest.of(0, 3, Sort.by("price"));
//        List<Dish> dishesByPrice = Stream.of(mushroomSoup, cheesecake, cappuccino, caesar)
//                .sorted(Comparator.comparing(Dish::getPrice))
//                .collect(Collectors.toList());
//        Page<Dish> pagesPublishersByPrice = new PageImpl<>(dishesByPrice, pagesWithThreeElementsSortedByPrice, dishesByPrice.size());
//
//        when(dishRepository.findAll(pagesWithThreeElementsSortiedByTitle)).thenReturn(pagesPublishersByTitle);
//        lenient().when(dishRepository.findAll(pagesWithThreeElementsSortedByPrice)).thenReturn(pagesPublishersByPrice);
//
//        List<FullDishDto> sortingByTitle = dishService.sortingBy("title", "0");
//        assertThat(sortingByTitle.get(0).getTitle()).isEqualTo("Caesar");
//        assertThat(sortingByTitle.get(0).getPrice()).isEqualTo("68");
//        assertThat(sortingByTitle.get(1).getTitle()).isEqualTo("Cappuccino");
//
//        List<FullDishDto> sortingByPrice = dishService.sortingBy("price", "0");
//        assertThat(sortingByPrice.get(0).getPrice()).isEqualTo("20");
//        assertThat(sortingByPrice.get(1).getPrice()).isEqualTo("33");
//
//    }
//
//    @DisplayName("JUNIT Test DishServiceImpl getDishByTitle() method --positive test case scenario")
//    @Test
//    void getByCategory_positiveTest() {
//        Dish dish = new Dish(5L, 30, "Green tea", 300, DRINKS, true, List.of(
//                new Ingredient(2L, List.of(new Food(2L, "Green tea", "Green tea Ahmad",
//                        true, LocalDate.of(2022, Month.DECEMBER, 1),
//                        LocalDate.of(2023, Month.JANUARY, 25),
//                        LocalDateTime.now(), LocalDateTime.now())), true, 5, 10)));
//
//        lenient().when(dishRepository.findDishByTitle("Green tea")).thenReturn(dish);
//        Assertions.assertEquals(dish.getTitle(), "Green tea");
//        System.out.printf("User is found :%s%n", dishService.getDishByTitle("Green tea"));
//    }
//
//    @DisplayName("JUNIT Test DishServiceImpl isStatusActive() method --positive test case scenario")
//    @Test
//    void testIsStatusActive() {
//        when(dishRepository.findDishByTitle("Sushi California")).thenReturn(dish);
//        assertThat(dishService.isStatusActive("Sushi California")).isTrue();
//    }
//}
