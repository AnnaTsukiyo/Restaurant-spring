package com.epam.zelener.restaurant.dtos;

import com.epam.zelener.restaurant.model.Dish;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderCreateDto {

    @NotBlank
    private String orderId;
    @NotBlank
    private List<Dish> dishList;
    @NotBlank
    private String totalPrice;
    @NotBlank
    @Size(min = 5)
    private String methodOfReceiving;
}
