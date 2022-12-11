package com.epam.zelener.restaurant.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RecipeCreateDto {

    @NotBlank
    private String id;
    @NotBlank
    private String number;
    @NotBlank
    private String title;
    @NotBlank
    private String duration;

}
