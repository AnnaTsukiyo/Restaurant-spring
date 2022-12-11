package com.epam.zelener.restaurant.dtos;

import lombok.*;

import javax.validation.constraints.Email;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserUpdateDto {

    private String fullName;
    @Email(message = "{wrong.email}")
    private String newEmail;

    private String password;
}
