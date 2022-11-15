package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullUserDto;
import com.epam.zelener.restaurant.dtos.UserSignUpDto;

import java.util.List;

public interface UserService {
    void createUser(UserSignUpDto userSignUpDto);

    void deleteUser(String phoneNumber);

    UserSignUpDto getUserByEmail(String email);
    UserSignUpDto getUserByPhoneNumber(String phoneNumber);
    void updateUser(UserSignUpDto userSignUpDto, String email);
    List<FullUserDto> getAllUsers();
    List<FullUserDto> getAllByPages(String page);
}
