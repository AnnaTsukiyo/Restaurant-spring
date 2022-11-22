package com.epam.zelener.restaurant.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDto {

    @NotBlank
    private long orderId;
    @NotBlank
    private String userId;
    @NotBlank
    private String dishId;
    @NotBlank
    private String totalPrice;
    @NotBlank
    private String methodOfReceiving;


}
