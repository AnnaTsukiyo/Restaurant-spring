package com.epam.zelener.restaurant.dtos;

import com.epam.zelener.restaurant.model.Dish;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderRequestDto {

    private String orderId;

    private List<Dish> dishList;

    private String totalPrice;

    private String methodOfReceiving;

}
