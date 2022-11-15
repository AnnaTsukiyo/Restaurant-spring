package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullManagerDto;
import com.epam.zelener.restaurant.dtos.ManagerRequestDto;

import java.util.List;

public interface ManagerService {

    void createManager(ManagerRequestDto requestManagerDto);

    void deleteManager(String name);

    ManagerRequestDto getManagerById(String id);

    void updateManager(ManagerRequestDto requestManagerDto, String name);

    List<FullManagerDto> getAllManager();
}
