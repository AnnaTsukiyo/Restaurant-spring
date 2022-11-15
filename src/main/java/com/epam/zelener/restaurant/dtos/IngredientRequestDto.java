package com.epam.zelener.restaurant.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IngredientRequestDto {
    @NotBlank
    private String id;
    @NotBlank
    private String Food;
    @NotBlank
    @Pattern(regexp = "^\\d{1,4}", message = "{wrong.quantity}")
    private String quantity;
    @NotBlank
    private String units;
    @NotBlank
    private String status;
}
