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
    private String id;
    @NotBlank
    private String number;
    @NotBlank
    private String isActive;
    @NotBlank
    @Size(min = 5, message = "{wrong.title}")
    private String title;
    @NotBlank
    private String duration;
    @NotBlank
    private String status;
    @NotBlank
    private List<Ingredient> ingredientList;
}
