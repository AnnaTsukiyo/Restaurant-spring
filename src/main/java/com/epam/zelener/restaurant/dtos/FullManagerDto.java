package com.epam.zelener.restaurant.dtos;

import com.epam.zelener.restaurant.model.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FullManagerDto {

    @NotBlank
    private String id;
    private String name;
    private String age;
    @NotBlank
    private List<User> userData;
    private String role;
    @NotBlank
    private String status;
    private String position;
    private String salary;
}
