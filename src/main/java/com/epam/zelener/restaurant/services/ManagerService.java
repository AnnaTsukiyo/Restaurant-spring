package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullManagerDto;
import com.epam.zelener.restaurant.dtos.ManagerRequestDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public interface ManagerService {

    @Transactional
    void createManager(ManagerRequestDto requestManagerDto);

    @Transactional
    void deleteManager(String name);

    @Transactional
    ManagerRequestDto getManagerByName(String name);

    @Transactional
    ManagerRequestDto getManagerById(String id);

    @Transactional
    void updateManager(ManagerRequestDto requestManagerDto, String name);

    @Transactional
    List<FullManagerDto> getAllManager();
}
