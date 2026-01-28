package com.example.ecomProject.services;

import com.example.ecomProject.exceptions.EmptyCartException;
import com.example.ecomProject.exceptions.OrderDoesntExistException;
import com.example.ecomProject.models.*;
import com.example.ecomProject.models.dtos.OrderResponse;
import com.example.ecomProject.repo.CartRepo;
import com.example.ecomProject.repo.OrderItemRepo;
import com.example.ecomProject.repo.OrderRepo;
import com.example.ecomProject.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest
{
    @Mock
    private UserRepo userRepo;
    @Mock
    private OrderRepo orderRepo;
    @Mock
    private CartRepo cartRepo;
    @Mock
    private OrderItemRepo orderItemRepo;

    @InjectMocks
    private OrderService orderService;

    @Test
    void placeOrder_shouldCreateOrder_andClearCart()
    {
        Product p1 = new Product();
        p1.setName("Keyboard");

        Product p2 = new Product();
        p2.setName("Mouse");

        CartItem ci1 = new CartItem();
        ci1.setProduct(p1);
        ci1.setQuantity(2);

        CartItem ci2 = new CartItem();
        ci2.setProduct(p2);
        ci2.setQuantity(3);

        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>(List.of(
                ci1,
                ci2
        )));
        cart.setTotalPrice(new BigDecimal("2500.00"));

        User user = new User();
        user.setUsername("X");
        user.setCart(cart);
        user.setOrders(new ArrayList<>());

        cart.setUser(user);

        when(userRepo.findByUsername("X")).thenReturn(user);

        List<OrderResponse> responses = orderService.placeOrder("X");

        verify(
                orderRepo,
                times(1)
        ).save(any(Order.class));

        verify(
                orderItemRepo,
                times(2)
        ).save(any(OrderItem.class));

        verify(
                cartRepo,
                times(1)
        ).save(cart);

        // cart should be empty
        assertEquals(
                0,
                cart.getCartItems()
                    .size()
        );
        assertEquals(
                BigDecimal.ZERO,
                cart.getTotalPrice()
        );

        assertEquals(
                1,
                user.getOrders()
                    .size()
        );
        assertEquals(
                1,
                responses.size()
        );

        OrderResponse response = responses.get(0);

        assertEquals(
                new BigDecimal("2500.00"),
                response.getTotalPrice()
        );
        assertEquals(
                2,
                response.getProduct_Quantity()
                        .get("Keyboard")
        );
        assertEquals(
                3,
                response.getProduct_Quantity()
                        .get("Mouse")
        );
    }

    @Test
    void placeOrderWhenCartIsEmpty_shouldThrowException()
    {
        Cart newCart = new Cart();
        newCart.setCartItems(new ArrayList<>());

        User newUser = new User();
        newUser.setName("X");
        newUser.setCart(newCart);

        when(userRepo.findByUsername("X")).thenReturn(newUser);

        assertThrows(EmptyCartException.class, () -> {
            orderService.placeOrder("X");
        });

        verify(
                orderRepo,
                never()
        ).save(any());

        verify(
                orderItemRepo,
                never()
        ).save(any());

        verify(
                cartRepo,
                never()
        ).save(any());
    }

    @Test
    void deleteOrder_shouldRestoreProductStock()
    {
        Product product = new Product();
        product.setQuantity(5);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(3);

        Order order = new Order();
        order.setOrderItems(List.of(orderItem));

        when(orderRepo.findById(1)).thenReturn(Optional.of(order));

        orderService.deleteOrder(1);

        verify(orderRepo, times(1)).delete(order);

        assertEquals(
                8,
                product.getQuantity()
        );
    }

    @Test
    void deleteNonExistentOrder_shouldThrowException()
    {
        when(orderRepo.findById(10)).thenReturn(Optional.empty());

        assertThrows(OrderDoesntExistException.class, () -> {
            orderService.deleteOrder(10);
        });

        verify(orderRepo, never()).delete(any());
    }
}
