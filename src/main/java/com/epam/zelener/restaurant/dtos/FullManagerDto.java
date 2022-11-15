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
public class FullManagerDto {

    private String id;
    @NotBlank
    @Size(min = 10, max = 30, message = "{wrong.name}")
    private String name;
    @NotBlank
    private String age;
    @NotBlank
    private String userPhoneNumber;
    @NotBlank
    private String role;
    @NotBlank
    private String status;
    @NotBlank
    private String position;
    @NotBlank
    @Pattern(regexp = "^\\d{1,5}", message = "{wrong.salary}")
    private String salary;
}
