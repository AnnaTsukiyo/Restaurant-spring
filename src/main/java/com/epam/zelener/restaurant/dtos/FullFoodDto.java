package com.epam.zelener.restaurant.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FullFoodDto {

    @NotBlank
    private String id;
    @NotBlank
    @Size(min = 5, message = "{wrong.title}")
    private String title;
    @NotBlank
    @Size(max = 200, message = "{wrong.description}")
    private String description;
    @NotBlank
    private String status;
    @NotBlank
    private String prodDate;
    @NotBlank
    private String expDate;
}
