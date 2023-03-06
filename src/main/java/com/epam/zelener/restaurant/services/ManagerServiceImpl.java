package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullManagerDto;
import com.epam.zelener.restaurant.dtos.ManagerCreateDto;
import com.epam.zelener.restaurant.dtos.ManagerRequestDto;
import com.epam.zelener.restaurant.model.Manager;
import com.epam.zelener.restaurant.model.Role;
import com.epam.zelener.restaurant.model.Status;
import com.epam.zelener.restaurant.repositories.ManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class ManagerServiceImpl implements ManagerService {

    @Resource
    private final ModelMapper mapper;

    private final ManagerRepository managerRepository;

    @Override
    @Transactional
    public Optional<FullManagerDto> createManager(ManagerCreateDto managerRequestDto) {
        log.info("createManager with name {}", managerRequestDto.getName());
        managerRepository.save(mapper.map(managerRequestDto, Manager.class));
        log.info("New manager is created with a name {}", managerRequestDto.getName());
        return getManagerByName(managerRequestDto.getName());
    }

    @Override
    @Transactional
    public FullManagerDto deactivateManager(String name) {
        log.info("deactivateManager by name {}", name);
        FullManagerDto managerDto = getManagerByName(name).orElseThrow();
        Optional<Manager> manager = managerRepository.findById(Long.valueOf((managerDto.getId())));
        managerRepository.updateStatus(name);
        manager.orElseThrow().setStatus(Status.valueOf("INACTIVE"));
        Manager deactivatedManager = managerRepository.save(manager.orElseThrow());
        log.info("Manager {} is deactivated", name);
        return mapper.map(deactivatedManager, FullManagerDto.class);
    }

    @Override
    @Transactional
    public Optional<FullManagerDto> getManagerByName(String name) {
        log.info("getManagerByName with name{}", name);
        try {
            return Optional.of(mapper.map(managerRepository.findManagerByName(name), FullManagerDto.class));
        } catch (IllegalArgumentException ex) {
            log.info("Manager with the name {} wasn't found! ", name);
            throw new NoSuchElementException();
        }
    }

    @Override
    @Transactional
    public Optional<FullManagerDto> getManagerById(String id) {
        log.info("getManagerById {}", id);
        try {
            return Optional.of(mapper.map(managerRepository.findManagerById(Long.parseLong(id)), FullManagerDto.class));
        } catch (IllegalArgumentException ex) {
            log.info("Manager with id {} wasn't found! ", id);
            throw new NoSuchElementException();
        }
    }

    @Override
    @Transactional
    public ManagerRequestDto updateManager(ManagerRequestDto managerRequestDto, String name) {
        log.info("updateManager by name {}", name);
        FullManagerDto fullManagerDto = getManagerByName(name).orElseThrow();
        Optional<Manager> manager = managerRepository.findById(Long.valueOf(fullManagerDto.getId()));

        String newName = managerRequestDto.getName() == null ? name : managerRequestDto.getName();
        String role = (managerRequestDto.getRole() == null ? fullManagerDto.getRole() : managerRequestDto.getRole());

        manager.orElseThrow().setName(newName);
        manager.orElseThrow().setRole(Role.valueOf(role));

        Manager updatedManager = managerRepository.save(manager.orElseThrow());
        log.info("Manager is updated successfully");
        return mapper.map(updatedManager, ManagerRequestDto.class);
}

    @Override
    @Transactional
    public FullManagerDto updateManagerName(String id, String name) {
        log.info("updateManagerName ", name);

        FullManagerDto fullManagerDto = getManagerById(id).orElseThrow();
        Manager manager = managerRepository.findManagerById(Long.parseLong(id));
        managerRepository.updateName(id, name);
        Manager updatedManager = managerRepository.save(manager);
        log.info("Manager is updated successfully");
        return mapper.map(updatedManager, FullManagerDto.class);
    }

    @Override
    @Transactional
    public List<FullManagerDto> getAllManager() {
        log.info("getAllManager method ");
        return managerRepository.findAll().stream()
                .map(e -> mapper.map(e, FullManagerDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isStatusActive(String name) {
        log.info("Checking if manager with such name {} is active", name);
        return Boolean.parseBoolean(String.valueOf(getManagerByName(name).orElseThrow().getStatus().equals("ACTIVE")));
    }
}
