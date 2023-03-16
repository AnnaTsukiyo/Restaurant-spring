package com.epam.zelener.restaurant.dtos;

import com.epam.zelener.restaurant.validation.EmailAlreadyExists;
import com.epam.zelener.restaurant.validation.WrongConfirmedPassword;
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
    @Pattern(regexp = "(^CUSTOMER$)?(^MAANGER$)?", message = "role must be 'CUSTOMER'")
    private String role;
    @NotBlank(message = "{empty.email}")
    @Email(message = "{wrong.email}")
    @EmailAlreadyExists
    private String email;
    @NotNull
    @Pattern(regexp = "^\\+?(38)?(\\d{10,11})$")
    @Size(min = 10, message = "{wrong.phoneNumber}")
    private String phoneNumber;
    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String dateOfBirth;
    @NotNull
    @Size(min = 6, max = 20, message = "{wrong.password}")
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})")
    private String password;
    @NotNull
    @WrongConfirmedPassword(message = "{passwordConfirmed}")
    private String passwordConfirmed;
}
