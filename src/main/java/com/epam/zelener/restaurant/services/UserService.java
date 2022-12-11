package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullUserDto;
import com.epam.zelener.restaurant.dtos.UserCreateDto;
import com.epam.zelener.restaurant.dtos.UserUpdateDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    @Transactional
    Optional<FullUserDto> createUser(UserCreateDto userCreateDto);

    @Modifying
    @Transactional
    FullUserDto deactivateUser(String phoneNumber);

    @Transactional
    Optional<FullUserDto> getUserByEmail(String email);

    @Transactional
    Optional<FullUserDto> getUserByPhoneNumber(String phoneNumber);

    @Modifying
    @Transactional
    UserUpdateDto updateUser(UserUpdateDto userUpdateDto, String email);

    @Modifying
    @Transactional
    FullUserDto updateUserAddress(String email, String address);

    @Transactional
    List<FullUserDto> getAllUsers();

    @Transactional
    List<FullUserDto> getAllByPages(String page);

    boolean isStatusActive(String email);
}
