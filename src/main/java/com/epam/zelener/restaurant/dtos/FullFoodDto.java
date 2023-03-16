package com.epam.zelener.restaurant.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FullFoodDto {

    @NotBlank
    private String id;
    private String title;
    private String description;
    @NotBlank
    private String status;
    @NotBlank
    private String isActive;
    private String prodDate;
    private String expDate;
}
