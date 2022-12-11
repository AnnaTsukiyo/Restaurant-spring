package com.epam.zelener.restaurant.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RecipeRequestDto {

    private String number;

    private String title;

    private String duration;
}
