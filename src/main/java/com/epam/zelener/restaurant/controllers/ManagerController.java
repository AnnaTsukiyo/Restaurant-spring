package com.epam.zelener.restaurant.controllers;

import com.epam.zelener.restaurant.dtos.FullManagerDto;
import com.epam.zelener.restaurant.dtos.ManagerCreateDto;
import com.epam.zelener.restaurant.dtos.ManagerRequestDto;
import com.epam.zelener.restaurant.exceptions.UserNotFoundSuchElementException;
import com.epam.zelener.restaurant.services.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/api/manager")

public class ManagerController {

    @Resource
    private ManagerService managerService;

    @Operation(summary = "Create a new manager")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "A new manager is created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data is provided. Manager is not created.", content = @Content)})
    @PostMapping("/create")
    public ResponseEntity<Object> createManager(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Object Manager is to be created")
                                                @RequestBody @Valid ManagerCreateDto managerRequestDto) {
        log.info("Request to create a new Manager :{}", managerRequestDto);
        managerService.createManager(managerRequestDto);
        return new ResponseEntity<>(managerRequestDto.getName() + " -- A new manager with name{} is created", HttpStatus.OK);
    }

    @Operation(summary = "Deactivate a manager by changing status to INACTIVE")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Manager's status is changed to INACTIVE successfully"),
            @ApiResponse(responseCode = "404", description = "Manager is not found"),
            @ApiResponse(responseCode = "409", description = "Manager is already INACTIVE")})
    @DeleteMapping(value = "/deactivate/{name}")
    public ResponseEntity<Object> deactivateManager (@PathVariable ("name") String name) {
        log.info("Request to deactivate Manager with a name:{}", name);
      try {
        if (!managerService.isStatusActive(name)) {
            log.warn("Manager with an name{} is already INACTIVE!!!", name);
            return new ResponseEntity<>(name + " –- Manager with a name{} is already INACTIVE", HttpStatus.CONFLICT);
        } else {
            managerService.deactivateManager(name);
            return new ResponseEntity<>(name + " –- Manager's status with a name {} is changed to INACTIVE", HttpStatus.OK);
        }
    } catch ( NoSuchElementException e) {
        log.error("Manager with a given name {} doesn't exist", name);
        return new ResponseEntity<>(name + " -- Manager with such name doesn't exist", HttpStatus.NOT_FOUND);
    }
}

    @Operation(summary = "Get all managers")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found all managers in database successfully"),
            @ApiResponse(responseCode = "404", description = "No managers are found. No managers data!")})
    @GetMapping("/all")
    public ResponseEntity<Object> findAllManagers() {
        log.info("Request to find all FullManagerDto :");
        List<FullManagerDto> managers = managerService.getAllManager();
        if (!managers.isEmpty()) {
            log.info("Users are found successfully");
            return new ResponseEntity<>(managers, HttpStatus.OK);
        } else {
            log.error("No managers are found! No managers data!");
            return new ResponseEntity<>("No managers data!", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get a manager by its id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found the manager successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ManagerRequestDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id provided", content = @Content),
            @ApiResponse(responseCode = "404", description = "Manager is not found", content = @Content)})

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getManagerById(@PathVariable String id) {
        try {
            if (managerService.getManagerById(id).isEmpty() || managerService.getManagerById(id) == null) {
                log.warn("There is no manager with a given id : {} !", id);
                return new ResponseEntity<>(id + " –- Invalid id provided.", HttpStatus.BAD_REQUEST);
            } else {
                Optional<FullManagerDto> manager = managerService.getManagerById(id);
                log.info("Request to get a ManagerRequestDto by the id :{}", id);
                return new ResponseEntity<>(manager, HttpStatus.OK);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(id + " -- Manager with such id {} doesn't exist ", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Update manager by its name")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "The manager is updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid name is  provided"),
            @ApiResponse(responseCode = "404", description = "Manager is not found")})

    @PatchMapping(value = "/{name}")
    public ResponseEntity<Object> updateManager(@Valid @RequestBody ManagerRequestDto managerRequestDto, @PathVariable String name) {
        try {
            managerService.updateManager(managerRequestDto, name);
            log.info("Request to update a Manager with a name:{}", name);
            return new ResponseEntity<>(name + " Manager with name {} is updated successfully", HttpStatus.OK);
        } catch (UserNotFoundSuchElementException e) {
            return new ResponseEntity<>(managerRequestDto.getName() + " --Manager with such email doesn't exist ", HttpStatus.NOT_FOUND);
        }
    }
}
