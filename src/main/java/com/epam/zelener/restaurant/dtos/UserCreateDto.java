package com.epam.zelener.restaurant.dtos;

import com.epam.zelener.restaurant.validation.EmailAlreadyExists;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserCreateDto {

    @NotNull
    @Size(min = 10, max = 40, message = "{wrong.lastName}")
    private String fullName;
    private String role;
    @NotNull
    @Email(message = "{wrong.email}")
    @EmailAlreadyExists
    private String email;
    @NotNull
    @Size(min = 6, max = 20, message = "{wrong.password}")
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})")
    private String password;
    private String passwordConfirmed;
}
