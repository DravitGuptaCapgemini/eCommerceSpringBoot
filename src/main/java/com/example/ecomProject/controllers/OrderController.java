package com.example.ecomProject.controllers;

import com.example.ecomProject.models.Order;
import com.example.ecomProject.models.dtos.OrderResponse;
import com.example.ecomProject.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController
{
    @Autowired
    private OrderService orderService;

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable int orderId)
    {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO --> Fetch the orders of a user based on its ID
    @GetMapping()
    private ResponseEntity<?> getOrders(Authentication auth)
    {
        String username = auth.getName();
        List<OrderResponse> orders = orderService.getOrders(username);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }


}
