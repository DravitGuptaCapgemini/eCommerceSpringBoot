package com.example.ecomProject.controllers;

import com.example.ecomProject.models.dtos.AddToCartRequest;
import com.example.ecomProject.models.dtos.CartResponse;
import com.example.ecomProject.models.dtos.OrderResponse;
import com.example.ecomProject.models.dtos.ProductResponse;
import com.example.ecomProject.repo.UserRepo;
import com.example.ecomProject.services.CartService;
import com.example.ecomProject.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController
{
    @Autowired
    private CartService srvce;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepo userRepo;

    @GetMapping()
    public ResponseEntity<?> showCartItems(Authentication auth)
    {
        String username = auth.getName();
        CartResponse response = srvce.createCartResponse(userRepo.findByUsername(username)
                                                                 .getCart());
        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCartItem(Authentication auth, @Valid @RequestBody AddToCartRequest cartItem)
    {
        CartResponse cartItems = srvce.addCartItem(
                auth,
                cartItem
        );
        return new ResponseEntity<>(
            cartItem,
            HttpStatus.OK
        );
    }

    @PutMapping("/empty")
    public ResponseEntity<?> emptyTheCart(Authentication auth)
    {
        CartResponse response = srvce.emptyTheCart(auth);
        return new ResponseEntity<>(
            response,
            HttpStatus.OK
        );
    }

    @PutMapping("/delete/{productId}")
    public ResponseEntity<?> deleteCartItem(Authentication auth, @PathVariable int productId)
    {
        CartResponse response = srvce.deleteCartItem(
                auth,
                productId
        );
        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

    @PostMapping("/place-order")
    public ResponseEntity<?> placeOrder(Authentication auth)
    {
        String username = auth.getName();
        List<OrderResponse> orderResponses = orderService.placeOrder(username);
        return new ResponseEntity<>(
                orderResponses,
                HttpStatus.OK
        );
    }

    @GetMapping("/get-related")
    public ResponseEntity<?> getRelatedProducts(Authentication auth)
    {
        HashMap<String, List<ProductResponse>> relatedProducts = srvce.getRelatedProducts(auth);
        return new ResponseEntity<>(relatedProducts, HttpStatus.OK);
    }
}
