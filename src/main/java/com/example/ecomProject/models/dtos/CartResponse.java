package com.example.ecomProject.models.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartResponse
{
    HashMap<String, Integer> item_Quantity = new HashMap<>();
    BigDecimal cartTotal;
}
