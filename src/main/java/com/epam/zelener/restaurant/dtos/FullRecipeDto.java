package com.epam.zelener.restaurant.dtos;

import com.epam.zelener.restaurant.model.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FullRecipeDto {

    private String id;
    private String number;
    @NotBlank
    private String isActive;
    private String title;
    private String duration;
    @NotBlank
    private String status;
    @NotBlank
    private List<Ingredient> ingredientList;
}
