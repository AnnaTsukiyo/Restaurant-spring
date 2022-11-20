package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullUserDto;
import com.epam.zelener.restaurant.dtos.UserSignUpDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public interface UserService {

    @Transactional
    void createUser(UserSignUpDto userSignUpDto);

    @Transactional
    void deleteUser(String phoneNumber);

    @Transactional
    UserSignUpDto getUserByEmail(String email);

    @Transactional
    UserSignUpDto getUserByPhoneNumber(String phoneNumber);

    @Transactional
    void updateUser(UserSignUpDto userSignUpDto, String email);

    @Transactional
    List<FullUserDto> getAllUsers();

    @Transactional
    List<FullUserDto> getAllByPages(String page);
}
