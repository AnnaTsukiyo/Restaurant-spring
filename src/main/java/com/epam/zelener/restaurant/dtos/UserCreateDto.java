package com.epam.zelener.restaurant.dtos;

import com.epam.zelener.restaurant.validation.EmailAlreadyExists;
import com.epam.zelener.restaurant.validation.PhoneNumberExists;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;

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
    @NotBlank
    @Pattern(regexp = "^\\+?(38)?(\\d{10,11})$")
    @Size(min = 10, message = "{wrong.phoneNumber}")
    @PhoneNumberExists
    private String phoneNumber;
    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String dateOfBirth;
    @NotNull
    @Size(min = 6, max = 20, message = "{wrong.password}")
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})")
    private String password;
    private String passwordConfirmed;
}
