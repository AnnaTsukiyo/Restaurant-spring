package com.epam.zelener.restaurant.dtos;

import com.epam.zelener.restaurant.model.Food;
import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FullIngredientDto {
    @NotBlank
    private String id;
    @NotNull
    private List<Food> foodList;
    @NotNull
    private String isActive;
    @NotNull
    @Pattern(regexp = "^\\d{1,4}", message = "{wrong.quantity}")
    private String quantity;
    @NotNull
    private String units;
    @NotNull
    private String status;
}
