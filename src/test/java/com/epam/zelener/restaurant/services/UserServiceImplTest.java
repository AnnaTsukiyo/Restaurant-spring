package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullUserDto;
import com.epam.zelener.restaurant.dtos.UserCreateDto;
import com.epam.zelener.restaurant.dtos.UserUpdateDto;
import com.epam.zelener.restaurant.exceptions.UserNotFoundSuchElementException;
import com.epam.zelener.restaurant.model.Role;
import com.epam.zelener.restaurant.model.User;
import com.epam.zelener.restaurant.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.epam.zelener.restaurant.model.Status.ACTIVE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    private final ModelMapper mapper = new ModelMapper();
    private User user;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(mapper, userRepository);
        user = new User(1L, "Jonas Tidermann", "+3806567898", "address", "admin@gmail.com", "passwordP1","passwordP1",
                "2000-01-01", Role.GUEST, ACTIVE, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());
    }

    @DisplayName("JUNIT Test UserServiceImpl getUserByEmail() method --positive test case scenario")
    @Test
    void getUserByEmail_positiveTest() {
        String email = "admin@gmail.com";
        User user = new User(1L, "Jonas Tidermann", "+3806567898", "address", email, "passwordP1","passwordP1",
                "2000-01-01", Role.GUEST, ACTIVE, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());
        FullUserDto userDto = new FullUserDto();
        userDto.setEmail(email);
        lenient().when(userRepository.findUserByEmail(email)).thenReturn(user);

        Assertions.assertEquals(user.getEmail(), email);

        System.out.println("User is found :" + userService.getUserByEmail(email));
    }

    @DisplayName("JUNIT Test UserServiceImpl getUserByEmail() method --- negative test case scenario ")
    @Test
    void getUserByEmail_negativeTest() {
        lenient().when(userRepository.findUserByEmail("admin@gmail.com")).thenThrow(UserNotFoundSuchElementException.class);
        Assertions.assertThrows(UserNotFoundSuchElementException.class, () -> {
            userService.getUserByEmail("admin@gmail.com").orElseThrow(UserNotFoundSuchElementException::new);
        });
        System.out.println("User is not found with email{} :" + "admin@gmail.com");
    }

    @DisplayName("JUNIT Test UserServiceImpl createUser() method --- positive test case scenario ")
    @Test
    void createUser_positiveTest() {

        UserCreateDto userDto = new UserCreateDto("Marti Schulz", "CUSTOMER","marti@gmail.com", "+3806567898","2000-01-01","Ppassword123", "Ppassword123");
        User user = mapper.map(userDto, User.class);
        lenient().when(userRepository.save(user)).thenReturn(user);
        lenient().when(userRepository.findUserByEmail("marti@gmail.com")).thenReturn(user);

        Optional<FullUserDto> createdUser = userService.createUser(userDto);
        assertThat(createdUser.orElseThrow().getEmail()).isEqualTo("marti@gmail.com");
    }

    @DisplayName("JUNIT Test UserServiceImpl updateUser() method --- positive test case scenario")
    @Test
    void updateUser_positiveTest() {
        lenient().when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        lenient().when(userRepository.save(user)).thenReturn(user);
        lenient().when(userRepository.findUserByEmail(user.getEmail())).thenReturn(user);

        UserUpdateDto editUserDto = new UserUpdateDto();

        editUserDto.setFullName("Johannes Tidermann");
        editUserDto.setPassword("PassP12345");

        UserUpdateDto updatedUserDto = userService.updateUser(editUserDto, user.getEmail());

        assertThat(updatedUserDto.getFullName()).isEqualTo("Johannes Tidermann");
        assertThat(updatedUserDto.getPassword()).isEqualTo("PassP12345");
    }

    @DisplayName("JUNIT Test UserServiceImpl updateUserAddress() method --- positive test case scenario")
    @Test
    void updateUserAddress_positiveTest() {
        lenient().when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        lenient().when(userRepository.save(user)).thenReturn(user);
        lenient().when(userRepository.findUserByEmail(user.getEmail())).thenReturn(user);

        FullUserDto editUserDto = new FullUserDto();
        editUserDto.setAddress("3/2 Segedska street, Odesa, Ukraine");
        userService.updateUserAddress(user.getEmail(), user.getAddress());

        assertThat(editUserDto.getAddress()).isEqualTo("3/2 Segedska street, Odesa, Ukraine");

    }

    @DisplayName("JUNIT Test UserServiceImpl getAllUsers() method --- positive test case scenario ")
    @Test
    void getAllUsers_positiveTest() {
        lenient().when(userRepository.findAll()).thenReturn(List.of(user));
        List<FullUserDto> list = userService.getAllUsers();
        Assertions.assertFalse(list.isEmpty());
        assertThat(list.get(0).getEmail()).isEqualTo(user.getEmail());
    }

    @DisplayName("JUNIT Test UserServiceImpl getAllByPages() method --- positive test case scenario ")
    @Test
    void getAllByPages_positiveTest() {
        Pageable pagesWithThreeElements = PageRequest.of(0, 3);
        List<User> users = List.of(user, new User(), new User());
        Page<User> pagesPublishers = new PageImpl<>(users, pagesWithThreeElements, users.size());

        when(userRepository.findAll(pagesWithThreeElements)).thenReturn(pagesPublishers);

        List<FullUserDto> fullUserDtoList = userService.getAllByPages("0");
        assertThat(fullUserDtoList.get(0).getFullName()).isEqualTo(user.getFullName());
        assertThat(fullUserDtoList.get(0).getEmail()).isEqualTo(String.valueOf(user.getEmail()));
        assertThat(fullUserDtoList.get(1).getFullName()).isNull();
    }

    @DisplayName("JUNIT Test UserServiceImpl getUserByPhoneNumber() method --positive test case scenario")
    @Test
    void getUserByPhoneNumber_positiveTest() {
        User user = new User(1L, "Jonas Tidermann", "+3806567898", "address", "admin@gmail.com", "passwordP1", "passwordP1",
                "2000-01-01", Role.GUEST, ACTIVE, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());
        FullUserDto userDto = new FullUserDto();
        userDto.setPhoneNumber("+3806567898");
        lenient().when(userRepository.findUserByPhoneNumber("+3806567898")).thenReturn(user);

        Assertions.assertEquals("+3806567898", user.getPhoneNumber());

        System.out.printf("User is found :%s%n", userService.getUserByPhoneNumber("+3806567898"));
    }

    @DisplayName("JUNIT Test UserServiceImpl getUserByPhoneNumber() method --- negative test case scenario ")
    @Test
    void getUserByPhoneNumber_negativeTest() {
        lenient().when(userRepository.findUserByPhoneNumber("+3806567898")).thenThrow(UserNotFoundSuchElementException.class);
        Assertions.assertThrows(UserNotFoundSuchElementException.class, () -> userService.getUserByPhoneNumber("+3806567898").orElseThrow(UserNotFoundSuchElementException::new));
        System.out.println("User is not found with phone number {} :" + "+3806567898");
    }

    @DisplayName("JUNIT Test UserServiceImpl isStatusActive() method --positive test case scenario")
    @Test
    void testIsStatusActive() {
        when(userRepository.findUserByEmail("admin@gmail.com")).thenReturn(user);
        assertThat(userService.isStatusActive("admin@gmail.com")).isTrue();
    }

    @DisplayName("JUNIT Test UserServiceImpl deactivateUser() method --positive test case scenario")
    @Test
    void deactivateUser_positiveTest(){
            lenient().when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
            lenient().when(userRepository.save(user)).thenReturn(user);
            lenient().when(userRepository.findUserByEmail(user.getEmail())).thenReturn(user);

            FullUserDto userDto = userService.deactivateUser(user.getEmail());
            assertThat(userDto.getStatus().equals("INACTIVE"));
    }
}

