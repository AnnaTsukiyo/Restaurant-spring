package com.epam.zelener.restaurant.dtos;

import com.epam.zelener.restaurant.validation.EmailAlreadyExists;
import com.epam.zelener.restaurant.validation.WrongConfirmedPassword;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@WrongConfirmedPassword(message = "Wrong confirmed password entered")
public class FullUserDto {
    @NotBlank
    private String id;
    @NotBlank
    @Size(min = 5, message = "{wrong.name}")
    private String fullName;
    @NotBlank
    @Pattern(regexp = "^\\+?(38)?(\\d{10,11})$")
    @Size(min = 10, message = "{wrong.phoneNumber}")
    private String phoneNumber;
    private String role;
    @NotBlank
    @Size(min = 10, max = 100, message = "{wrong.address}")
    private String address;
    @NotBlank
    private String status;
    @NotBlank
    private String dateOfBirth;
    @NotBlank
    @Email
    @EmailAlreadyExists
    private String email;
    @NotBlank
    @Size(min = 6, max = 20, message = "{wrong.password}")
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})")
    private String password;
}
