package com.example.ecomProject.models.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest
{
    private String name;

    private String description;

    private String brand;

    @DecimalMin(value = "1.0", message = "Price must be at least 1.0")
    private BigDecimal price;

    private String category;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
