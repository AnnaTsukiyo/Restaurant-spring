package com.epam.zelener.restaurant.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ManagerRequestDto {

    private String name;
    private String role;
    private String position;
}
