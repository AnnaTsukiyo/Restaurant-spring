package com.epam.zelener.restaurant.controllers;

import com.epam.zelener.restaurant.dtos.FullManagerDto;
import com.epam.zelener.restaurant.dtos.ManagerRequestDto;
import com.epam.zelener.restaurant.services.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping("/create")
    public void createManager(@RequestBody @Valid ManagerRequestDto managerRequestDto) {
        log.info("Request to create a new Manager :{}", managerRequestDto);
        managerService.createManager(managerRequestDto);
    }

    @DeleteMapping(value = "/{name}")
    public ResponseEntity<Void> deleteManager(@PathVariable String name) {
        log.info("Request to delete a Manager with the name :{}", name);
        managerService.deleteManager(name);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public List<FullManagerDto> findAllUsers() {
        log.info("Request to find all FullManagerDto :{}");
        return managerService.getAllManager();
    }

    @GetMapping("/get/{id}")
    public ManagerRequestDto getManagerById(@PathVariable String id) {
        log.info("Request to get a ManagerRequestDto by the id :{}", id);
        return managerService.getManagerById(id);
    }

    @PutMapping(value = "/{name}")
    public void updateManager(@Valid @RequestBody ManagerRequestDto managerRequestDto, @PathVariable String name) {
        log.info("Request to update a Manager with a name:{}", name);
        managerService.updateManager(managerRequestDto, name);
    }
}
