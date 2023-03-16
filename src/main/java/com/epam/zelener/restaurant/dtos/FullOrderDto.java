package com.epam.zelener.restaurant.dtos;

import com.epam.zelener.restaurant.model.Dish;
import com.epam.zelener.restaurant.model.OrderStatus;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FullOrderDto {

    private String orderId;
    private String userId;
    @NotBlank
    private List<Dish> dishList;
    private String totalPrice;
    private String methodOfReceiving;
    @NotBlank
    private OrderStatus status;

}
