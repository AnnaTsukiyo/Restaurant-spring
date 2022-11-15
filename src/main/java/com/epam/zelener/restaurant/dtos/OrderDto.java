package com.epam.zelener.restaurant.dtos;

import com.epam.zelener.restaurant.model.OrderStatus;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDto {

    @NotBlank
    private long orderId;
    @NotBlank
    private Long userId;
    @NotBlank
    private Long dishId;
    @NotBlank
    private Double totalPrice;
    @NotBlank
    private LocalDateTime created;
    @NotBlank
    private String methodOfReceiving;
    @NotBlank
    private OrderStatus status;

}
