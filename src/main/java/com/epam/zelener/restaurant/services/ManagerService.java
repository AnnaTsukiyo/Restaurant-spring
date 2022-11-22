package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullManagerDto;
import com.epam.zelener.restaurant.dtos.ManagerRequestDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public interface ManagerService {

    @Transactional
    void createManager(ManagerRequestDto requestManagerDto);

    @Modifying
    @Transactional
    void deleteManager(String name);

    @Transactional
    ManagerRequestDto getManagerByName(String name);

    @Transactional
    ManagerRequestDto getManagerById(String id);

    @Modifying
    @Transactional
    void updateManager(ManagerRequestDto requestManagerDto, String name);

    @Modifying
    @Transactional
    void updateManagerName(long id, String name);

    @Transactional
    List<FullManagerDto> getAllManager();
}
