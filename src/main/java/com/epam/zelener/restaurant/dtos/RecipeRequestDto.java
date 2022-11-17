package com.epam.zelener.restaurant.dtos;

import com.epam.zelener.restaurant.model.Ingredient;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RecipeRequestDto {
    @NotBlank
    private int number;
    @NotBlank
    @Size(min = 5, message = "{wrong.title}")
    private String title;
    @NotBlank
    private String isActive;
    @NotBlank
    private int duration;
    @NotBlank
    private String status;
    @NotBlank
    private List<Ingredient> ingredientList;
}
