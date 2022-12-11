package com.epam.zelener.restaurant.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IngredientRequestDto {

    private String id;

    private String quantity;

    private String units;


}
