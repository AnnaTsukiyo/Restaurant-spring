package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullUserDto;
import com.epam.zelener.restaurant.dtos.UserCreateDto;
import com.epam.zelener.restaurant.dtos.UserUpdateDto;
import com.epam.zelener.restaurant.exceptions.UserNotFoundSuchElementException;
import com.epam.zelener.restaurant.model.Status;
import com.epam.zelener.restaurant.model.User;
import com.epam.zelener.restaurant.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
public class UserServiceImpl implements UserService {

    @Resource
    private final ModelMapper mapper;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public Optional<FullUserDto> createUser(UserCreateDto userCreateDto) {
        log.info("createUser with email {}", userCreateDto.getEmail());
        userRepository.save(mapper.map(userCreateDto, User.class));
        log.info("User was created successfully");
        return getUserByEmail(userCreateDto.getEmail());
    }

    @Transactional
    @Override
    public FullUserDto deactivateUser(String email) {
        log.info("deactivateUser with email {}", email);
        FullUserDto userDto = getUserByEmail(email).orElseThrow();
        Optional<User> user = userRepository.findById(Integer.valueOf(userDto.getId()));
        userRepository.updateStatus(email);
        user.orElseThrow().setStatus(Status.valueOf("INACTIVE"));
        User deactivatedUser = userRepository.save(user.orElseThrow());
        log.info("User {} is deactivated", email);
        return mapper.map(deactivatedUser, FullUserDto.class);
    }

    @Transactional
    @Override
    public Optional<FullUserDto> getUserByEmail(String email) {
        log.info("getUserByEmail with email: {}", email);
        try {
            return Optional.of(mapper.map(userRepository.findUserByEmail(email), FullUserDto.class));
        } catch (IllegalArgumentException ex) {
            log.info("User with email {} wasn't found! ", email);
            throw new UserNotFoundSuchElementException();
        }
    }

    @Transactional
    @Override
    public Optional<FullUserDto> getUserByPhoneNumber(String phone) {
        log.info("getUserByPhoneNumber with an phone : {}", phone);
        try {
            return Optional.of(mapper.map(userRepository.findUserByPhoneNumber(phone), FullUserDto.class));
        } catch (IllegalArgumentException ex) {
            log.info("User with phone number {} wasn't found! ", phone);
            throw new UserNotFoundSuchElementException();
        }
    }

    @Transactional
    @Override
    public UserUpdateDto updateUser(UserUpdateDto userUpdateDto, String email) {
        FullUserDto fullDto = getUserByEmail(email).orElseThrow();
        log.info("updateUser by email : {}", email);
        Optional<User> user = userRepository.findById(Integer.valueOf(fullDto.getId()));

        String newFullName = userUpdateDto.getFullName() == null ? fullDto.getFullName() : userUpdateDto.getFullName();
        String newEmail = userUpdateDto.getNewEmail() == null ? email : userUpdateDto.getNewEmail();
        String newPassword = userUpdateDto.getPassword() == null ? fullDto.getPassword() : userUpdateDto.getPassword();
        user.orElseThrow().setEmail(newEmail);
        user.orElseThrow().setFullName(newFullName);
        user.orElseThrow().setPassword(newPassword);

        User updatedUser = userRepository.save(user.orElseThrow());
        return mapper.map(updatedUser, UserUpdateDto.class);
    }

    @Override
    @Transactional
    public FullUserDto updateUserAddress(String email, String address) {
        log.info("updateUserAddress by email : {} ", email);
        getUserByEmail(email).orElseThrow();
        User user = userRepository.findUserByEmail(email);
        userRepository.updateAddress(email, address);
        User updatedUser = userRepository.save(user);
        log.info("User is updated successfully");
        return mapper.map(updatedUser, FullUserDto.class);
    }

    @Override
    @Transactional
    public List<FullUserDto> getAllUsers() {
        log.info("getAllUsers method ");
        return userRepository.findAll().stream()
                .map(e -> mapper.map(e, FullUserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<FullUserDto> getAllByPages(String page) {
        Pageable pagesWithThreeElements = PageRequest.of(Integer.parseInt(page), 3);
        log.info("Start method getAllByPages() in user service");
        return userRepository.findAll(pagesWithThreeElements)
                .stream()
                .map(e -> mapper.map(e, FullUserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isStatusActive(String email) {
        log.info("Checking if user with such email {} is active", email);
        return Boolean.parseBoolean(String.valueOf(getUserByEmail(email).orElseThrow().getStatus().equals("ACTIVE")));
    }
}
