package com.epam.zelener.restaurant.dtos;

import com.epam.zelener.restaurant.model.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FullManagerDto {

    private String id;
    @NotBlank
    @Size(min = 5, max = 30)
    private String name;
    @NotBlank
    private String age;
    @NotBlank
    private List<User> userData;
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
