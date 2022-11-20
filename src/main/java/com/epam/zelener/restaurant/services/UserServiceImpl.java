package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullUserDto;
import com.epam.zelener.restaurant.dtos.UserSignUpDto;
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
import java.util.NoSuchElementException;
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
    public void createUser(UserSignUpDto userSignUpDto) {
        log.info("createUser with phone number {}", userSignUpDto.getPhoneNumber());
        userRepository.save(mapper.map(userSignUpDto, User.class));
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        log.info("deleteUser with email {}", email);
        userRepository.updateStatus(email);
    }

    @Transactional
    @Override
    public UserSignUpDto getUserByEmail(String email) {
        log.info("getUserByEmail with email {}", email);
        try {
            return mapper.map(userRepository.findUserByEmail(email), UserSignUpDto.class);
        } catch (IllegalArgumentException ex) {
            throw new NoSuchElementException();
        }
    }

    @Transactional
    @Override
    public UserSignUpDto getUserByPhoneNumber(String phone) {
        log.info("getUserByPhoneNumber with an phone {}", phone);
        return mapper.map(userRepository.findUserByPhoneNumber(phone), UserSignUpDto.class);
    }

    @Transactional
    @Override
    public void updateUser(UserSignUpDto userSignUpDto, String email) {
        log.info("updateUser by email{}", email);
        User user = mapper.map(userRepository.findUserByEmail(email), User.class);
        userRepository.updateStatus(email);
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
}
