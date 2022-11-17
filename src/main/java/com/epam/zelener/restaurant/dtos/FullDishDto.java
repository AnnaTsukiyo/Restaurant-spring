package com.epam.zelener.restaurant.dtos;

import com.epam.zelener.restaurant.model.Ingredient;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FullDishDto {

    @NotBlank
    private String id;
    @NotBlank
    @Size(min = 5, message = "{wrong.title}")
    private String title;
    @NotBlank
    @Pattern(regexp = "^\\d{1,3}", message = "{wrong.price}")
    private String price;
    @NotBlank
    private String isActive;
    @NotBlank
    @Pattern(regexp = "^\\d{1,4}", message = "{wrong.weight}")
    private String weight;
    @NotBlank
    private String category;
    @NotBlank
    private String status;
    @NotBlank
    private List<Ingredient> ingredientList;
}
