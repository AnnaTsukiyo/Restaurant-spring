package com.epam.zelener.restaurant.dtos;

import com.epam.zelener.restaurant.model.Food;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FullIngredientDto {

    private String id;
    @NotBlank
    private List<Food> foodList;
    private String isActive;
    private String quantity;
    private String units;

}
