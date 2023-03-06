package com.epam.zelener.restaurant.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DishRequestDto {


    private String title;

    private String price;

    private String weight;
}
