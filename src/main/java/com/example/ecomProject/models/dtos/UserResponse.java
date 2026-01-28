package com.example.ecomProject.models.dtos;

import com.example.ecomProject.models.Cart;
import com.example.ecomProject.models.CartItem;
import com.example.ecomProject.models.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse
{
    private String name;
    private String username;
    private String role;
    private CartResponse cartResponse;
    private List<OrderResponse> orderResponses;
}
