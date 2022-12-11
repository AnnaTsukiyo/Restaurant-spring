package com.epam.zelener.restaurant.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DishCreateDto {
    @NotBlank
    @Size(min = 5, message = "{wrong.title}")
    private String title;
    @NotBlank
    @Pattern(regexp = "^\\d{1,3}", message = "{wrong.price}")
    private String price;
    @NotBlank
    @Pattern(regexp = "^\\d{1,4}", message = "{wrong.weight}")
    private String weight;
    @NotBlank
    private String category;

}
