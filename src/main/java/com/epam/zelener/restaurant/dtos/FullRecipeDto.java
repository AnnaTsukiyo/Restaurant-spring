package com.epam.zelener.restaurant.dtos;

import com.epam.zelener.restaurant.model.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FullRecipeDto {
    @NotBlank
    private int id;
    @NotBlank
    private int number;
    @NotBlank
    @Size(min = 5, message = "{wrong.title}")
    private String title;
    @NotBlank
    private int duration;
    @NotBlank
    private String status;
    @NotBlank
    private List<Ingredient> ingredientList;
}
