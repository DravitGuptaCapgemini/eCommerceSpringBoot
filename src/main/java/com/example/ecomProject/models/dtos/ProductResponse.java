package com.example.ecomProject.models.dtos;

import com.example.ecomProject.models.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductResponse
{
    private int productID;
    private String productName;
    private String brand;
    private BigDecimal price;
    private int inStock;
}
