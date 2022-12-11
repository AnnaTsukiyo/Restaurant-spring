package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullManagerDto;
import com.epam.zelener.restaurant.dtos.ManagerCreateDto;
import com.epam.zelener.restaurant.dtos.ManagerRequestDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public interface ManagerService {

    @Transactional
    Optional<FullManagerDto> createManager(ManagerCreateDto managerRequestDto);

    @Modifying
    @Transactional
    FullManagerDto deactivateManager(String name);

    @Transactional
    Optional<FullManagerDto> getManagerByName(String name);

    @Transactional
    Optional<FullManagerDto> getManagerById(String id);

    @Modifying
    @Transactional
    ManagerRequestDto updateManager(ManagerRequestDto requestManagerDto, String name);

    @Modifying
    @Transactional
    FullManagerDto updateManagerName(String id, String name);

    @Transactional
    List<FullManagerDto> getAllManager();

    boolean isStatusActive(String name);
}
