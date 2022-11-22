package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullManagerDto;
import com.epam.zelener.restaurant.dtos.ManagerRequestDto;
import com.epam.zelener.restaurant.model.Manager;
import com.epam.zelener.restaurant.repositories.ManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class ManagerServiceImpl implements ManagerService {

    @Resource
    private ModelMapper mapper;

    private final ManagerRepository managerRepository;

    @Override
    @Transactional
    public void createManager(ManagerRequestDto managerRequestDto) {
        Manager manager = mapper.map(managerRequestDto, Manager.class);
        log.info("New manager is created with a name {}", managerRequestDto.getName());
        managerRepository.save(manager);
    }

    @Override
    @Transactional
    public void deleteManager(String name) {
        log.info("deleteManager ny name {}", name);
        managerRepository.updateStatus(name);
    }

    @Override
    @Transactional
    public ManagerRequestDto getManagerByName(String name) {
        log.info("getManagerByName {}", name);
        return mapper.map(managerRepository.findManagerByName(name), ManagerRequestDto.class);
    }

    @Override
    @Transactional
    public ManagerRequestDto getManagerById(String id) {
        log.info("getManagerById {}", id);
        return mapper.map(managerRepository.findManagerById(id), ManagerRequestDto.class);
    }

    @Override
    @Transactional
    public void updateManager(ManagerRequestDto managerRequestDto, String name) {
        log.info("updateManager by name {}", name);
        Manager manager = mapper.map(managerRequestDto, Manager.class);
         managerRepository.save(manager);
    }

    @Override
    public void updateManagerName(long id, String name) {
        log.info("updateManagerName ", name);
        managerRepository.updateName(id, name);
    }

    @Override
    @Transactional
    public List<FullManagerDto> getAllManager() {
        log.info("getAllManager method ");
        return managerRepository.findAll().stream()
                .map(e -> mapper.map(e, FullManagerDto.class))
                .collect(Collectors.toList());
    }
}
