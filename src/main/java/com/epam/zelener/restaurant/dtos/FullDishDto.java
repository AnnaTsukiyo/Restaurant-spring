package com.epam.zelener.restaurant.dtos;

import com.epam.zelener.restaurant.model.Ingredient;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FullDishDto {

    @NotBlank
    private String id;
    private String title;
    private String price;
    @NotBlank
    private String isActive;
    private String weight;
    private String category;
    @NotBlank
    private String status;
    @NotBlank
    private List<Ingredient> ingredientList;
}
