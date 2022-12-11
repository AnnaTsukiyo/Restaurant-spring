package com.epam.zelener.restaurant.dtos;

import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IngredientCreateDto {
    @NotBlank
    private String id;
    @NotNull
    @Pattern(regexp = "^\\d{1,4}", message = "{wrong.quantity}")
    private String quantity;
    @NotNull
    private String units;
    @NotNull
    private String status;

}