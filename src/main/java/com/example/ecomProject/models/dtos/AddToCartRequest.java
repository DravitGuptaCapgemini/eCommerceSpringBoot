package com.example.ecomProject.models.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddToCartRequest
{
    @NotNull
    @Min(value = 1)
    private int productId;
    @NotNull
    @Min(value = 1)
    private int quantity;
}
