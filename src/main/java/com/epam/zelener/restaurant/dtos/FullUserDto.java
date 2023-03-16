package com.epam.zelener.restaurant.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FullUserDto {
    private String id;
    private String fullName;
    private String phoneNumber;
    private String role;
    private String address;
    private String status;
    private String dateOfBirth;
    private String email;
    private String password;
    private String confirmedPassword;
}
