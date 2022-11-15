package com.epam.zelener.restaurant.dtos;

import com.epam.zelener.restaurant.validation.EmailAlreadyExists;
import com.epam.zelener.restaurant.validation.PhoneNumberExists;
import com.epam.zelener.restaurant.validation.WrongConfirmedPassword;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@WrongConfirmedPassword(message = "Wrong confirmed password entered")
public class UserSignUpDto {

    @NotBlank
    @Size(min = 10, max = 40, message = "{wrong.lastName}")
    private String fullName;
    @Pattern(regexp = "^\\+?(38)?(\\d{10,11})$")
    @NotBlank
    @Size(min = 10, message = "{wrong.phoneNumber}")
    @PhoneNumberExists
    private String phoneNumber;
    private String role;
    @NotEmpty
    private String dateOfBirth;
    @NotBlank
    private String isActive;
    @NotEmpty
    @Email(message = "{wrong.email}")
    @EmailAlreadyExists
    private String email;
    @NotEmpty
    @Size(min = 6, max = 20, message = "{wrong.password}")
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})")
    private String password;
    private String passwordConfirmed;
}
