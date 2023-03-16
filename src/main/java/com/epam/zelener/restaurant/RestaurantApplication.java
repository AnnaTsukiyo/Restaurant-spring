package com.epam.zelener.restaurant;

import com.epam.zelener.restaurant.dtos.UserCreateDto;
import com.epam.zelener.restaurant.services.DishService;
import com.epam.zelener.restaurant.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@Log4j2
public class RestaurantApplication {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    public static void main(String[] args) {
        SpringApplication.run(RestaurantApplication.class, args);
    }

    @Bean()
    CommandLineRunner init(UserService userService, DishService dishService) {
        return args -> {
            UserCreateDto createUserDto = new UserCreateDto();
            createUserDto.setRole("MANAGER");
            createUserDto.setFullName("Jonas Tidermann");
            createUserDto.setEmail("manager_jonas@gmail.com");
            createUserDto.setPassword("secretPassword12345");
            userService.createUser(createUserDto);

        };
    }
}
