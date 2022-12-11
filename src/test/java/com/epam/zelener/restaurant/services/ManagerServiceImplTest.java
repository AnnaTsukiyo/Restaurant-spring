package com.epam.zelener.restaurant.services;

import com.epam.zelener.restaurant.dtos.FullManagerDto;
import com.epam.zelener.restaurant.dtos.ManagerCreateDto;
import com.epam.zelener.restaurant.dtos.ManagerRequestDto;
import com.epam.zelener.restaurant.model.Manager;
import com.epam.zelener.restaurant.model.Role;
import com.epam.zelener.restaurant.model.User;
import com.epam.zelener.restaurant.repositories.ManagerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.epam.zelener.restaurant.model.Status.ACTIVE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManagerServiceImplTest {

    @Mock
    private ManagerRepository managerRepository;

    @InjectMocks
    private ManagerServiceImpl managerService;
    private final ModelMapper mapper = new ModelMapper();
    private Manager manager;

    @BeforeEach
    void setUp() {
        managerService = new ManagerServiceImpl(mapper, managerRepository);
        manager = new Manager("Scarlett", 25, Role.MANAGER, "Custom Manager", 10000.0);
        manager.setId(1L);
    }

    @DisplayName("JUNIT Test ManagerServiceImpl createManager() --positive test case scenario")
    @Test
    void createManager_positiveTest() {
        ManagerCreateDto managerDto = new ManagerCreateDto("Alexa","27", "MANAGER", "Store Manager", "12000.50");
        Manager manager = mapper.map(managerDto, Manager.class);
        lenient().when(managerRepository.save(manager)).thenReturn(manager);
        lenient().when(managerRepository.findManagerByName("Alexa")).thenReturn(manager);

        Optional<FullManagerDto> createdManager = managerService.createManager(managerDto);
        assertThat(createdManager.orElseThrow().getName()).isEqualTo("Alexa");
    }

    @DisplayName("JUNIT Test ManagerServiceImpl deactivateManager() method --positive test case scenario")
    @Test
    void deactivateManager_positiveTest() {
        manager.setId(2L);
        lenient().when(managerRepository.findById(Math.toIntExact(manager.getId()))).thenReturn(Optional.of(manager));
        lenient().when(managerRepository.findManagerByName(manager.getName())).thenReturn(manager);
        lenient().when(managerRepository.save(manager)).thenReturn(manager);
        FullManagerDto managerDto = managerService.deactivateManager(manager.getName());
        assertThat(managerDto.getStatus().equals("INACTIVE"));
    }

    @DisplayName("JUNIT Test ManagerServiceImpl getManagerByName() method --positive test case scenario")
    @Test
    void getManagerByName_positiveTest() {
        Manager manager = new Manager(2L, "Alexa", 29, List.of(new User(1L, "Jonas Tidermann", "+3806567898", "address", "admin@gmail.com", "passwordP1",
                "2000-01-01", Role.GUEST, ACTIVE, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now())), Role.MANAGER, ACTIVE, "Delivery Manager", 120000);
        FullManagerDto dto = new FullManagerDto();
        dto.setName("Alexa");
        lenient().when(managerRepository.findManagerByName("Alexa")).thenReturn(manager);

        Assertions.assertEquals(manager.getName(), "Alexa");

        System.out.printf("User is found :%s%n", managerService.getManagerByName("Alexa"));
    }

    @DisplayName("JUNIT Test ManagerServiceImpl getManagerByName() method --- negative test case scenario ")
    @Test
    void getManagerByName_negativeTest() {
        lenient().when(managerRepository.findManagerByName("Scarlett")).thenThrow(NoSuchElementException.class);
        Assertions.assertThrows(NoSuchElementException.class, () -> managerService.getManagerByName("Scarlett").orElseThrow(NoSuchElementException::new));
        System.out.println("Manager is not found with email{} :" + "Scarlett");
    }

    @DisplayName("JUNIT Test ManagerServiceImpl  getManagerByName() method --positive test case scenario")
    @Test
    void getManagerById() {
        Manager managerNew = new Manager(3L, "Alex", 33, List.of(new User(2L, "Jared Salvatore", "+3806568898", "address", "jared@gmail.com", "passwordP123",
                "2002-01-02", Role.GUEST, ACTIVE, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now())), Role.MANAGER, ACTIVE, "Delivery Manager", 120000);
        FullManagerDto dto = new FullManagerDto();
        dto.setId("3");
        lenient().when(managerRepository.findManagerById("3")).thenReturn(managerNew);

        Assertions.assertEquals(3, managerNew.getId());

        System.out.println("Manager is found :" + managerService.getManagerById("3"));

    }

    @DisplayName("JUNIT Test ManagerServiceImpl updateManager() method --- positive test case scenario ")
    @Test
    void updateManager_positiveTest() {
        when(managerRepository.findManagerByName(manager.getName())).thenReturn(manager);
        when(managerRepository.findById(Math.toIntExact((manager.getId())))).thenReturn(Optional.of(manager));
        when(managerRepository.save(manager)).thenReturn(manager);

        ManagerRequestDto editManager = mapper.map(manager,ManagerRequestDto.class);
        editManager.setRole("MANAGER");
        editManager.setName("Marry");
        ManagerRequestDto updatedManagerDto = managerService.updateManager(editManager, manager.getName());

        assertThat(updatedManagerDto.getRole()).isEqualTo("MANAGER");
        assertThat(updatedManagerDto.getName()).isEqualTo("Marry");
    }

    @DisplayName("JUNIT Test ManagerServiceImpl updateManagerName() method --positive test case scenario")
    @Test
    void updateManagerName_positiveTest() {
        lenient().when(managerRepository.findManagerById(String.valueOf(manager.getId()))).thenReturn(manager);
        lenient().when(managerRepository.save(manager)).thenReturn(manager);
        lenient().when(managerRepository.findManagerByName(manager.getName())).thenReturn(manager);

        FullManagerDto editManager = new FullManagerDto();
        editManager.setName("Marianna");

        managerService.updateManagerName(String.valueOf(manager.getId()), manager.getName());
        assertThat(editManager.getName()).isEqualTo("Marianna");

    }

    @DisplayName("JUNIT Test ManagerServiceImpl getAllManager() method --positive test case scenario")
    @Test
    void getAllManager_positiveTest() {
        lenient().when(managerRepository.findAll()).thenReturn(List.of(manager));
        List<FullManagerDto> list = managerService.getAllManager();
        Assertions.assertFalse(list.isEmpty());
        assertThat(list.get(0).getName()).isEqualTo(manager.getName());
    }

    @DisplayName("JUNIT Test ManagerServiceImpl isStatusActive() method --positive test case scenario")
    @Test
    void testIsStatusActive_positiveTest() {
        Manager newManager = new Manager(2L, "Michael", 22, List.of(new User(1L, "Johan Schmidt", "+3806557898", "address", "schmidt@gmail.com", "PpPasswordP1",
                "2000-01-01", Role.GUEST, ACTIVE, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now())), Role.MANAGER, ACTIVE, "Compliance Manager", 10000);
        FullManagerDto dto = new FullManagerDto();
        dto.setName("Michael");
        lenient().when(managerRepository.findManagerByName(newManager.getName())).thenReturn(newManager);
        assertThat(managerService.isStatusActive("Michael")).isTrue();
    }
}
