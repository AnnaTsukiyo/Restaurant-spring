package com.epam.zelener.restaurant.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ManagerCreateDto {

    @NotBlank
    @Size(min = 5, max = 30)
    private String name;
    @NotBlank
    private String age;
    @NotBlank
    private String role;
    @NotBlank
    private String position;
    @NotBlank
    @Pattern(regexp = "^\\d{1,5}", message = "{wrong.salary}")
    private String salary;
}
