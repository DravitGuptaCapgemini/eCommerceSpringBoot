package com.example.ecomProject.services;

import com.example.ecomProject.exceptions.UserAlreadyExistsException;
import com.example.ecomProject.exceptions.UsernameDoesntExist;
import com.example.ecomProject.models.*;
import com.example.ecomProject.models.dtos.CartResponse;
import com.example.ecomProject.models.dtos.UserRequest;
import com.example.ecomProject.models.dtos.UserResponse;
import com.example.ecomProject.repo.ProductRepo;
import com.example.ecomProject.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService
{
    @Autowired
    private UserRepo rp;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse save(UserRequest user)
    {
        if (rp.existsByUsername(user.getUsername()))
            throw new UserAlreadyExistsException(user.getUsername());

        User newUser = new User();
        newUser.setRole("USER");
        newUser.setName(user.getName());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        rp.save(newUser);

        cartService.createCart(newUser);

        UserResponse savedUser = new UserResponse();
        savedUser.setName(newUser.getName());
        savedUser.setUsername(newUser.getUsername());
        savedUser.setRole("USER");
        savedUser.setCartResponse(cartService.createCartResponse(newUser.getCart()));
        savedUser.setOrderResponses(orderService.ordersToResponses(newUser.getOrders()));

        return savedUser;
    }

    public UserResponse saveAdmin(UserRequest user)
    {
        if (rp.existsByUsername(user.getUsername()))
            throw new UserAlreadyExistsException(user.getUsername());

        User newUser = new User();
        newUser.setRole("ADMIN");
        newUser.setName(user.getName());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        rp.save(newUser);

        cartService.createCart(newUser);

        UserResponse savedUser = new UserResponse();
        savedUser.setName(newUser.getName());
        savedUser.setUsername(newUser.getUsername());
        savedUser.setRole("ADMIN");
        savedUser.setCartResponse(cartService.createCartResponse(newUser.getCart()));
        savedUser.setOrderResponses(orderService.ordersToResponses(newUser.getOrders()));
        return savedUser;
    }

    public void deleteUser(String username)
    {
        if (!rp.existsByUsername(username))
            throw new UsernameDoesntExist();

        User userToBeDeleted = rp.findByUsername(username);

        // Update the stock of the products which were inside their cart
        Cart userCart = userToBeDeleted.getCart();
        List<CartItem> items = userCart.getCartItems();

        if (items.isEmpty() == false)
        {
            for (CartItem item : items)
            {
                Product prod = item.getProduct();
                prod.setQuantity(prod.getQuantity() + item.getQuantity());
            }
        }

        // update the stock of the products which where inside their orders
        List<Order> userOrders = userToBeDeleted.getOrders();
        for (Order o : userOrders)
        {
            List<OrderItem> oItems = o.getOrderItems();
            for (OrderItem item : oItems)
            {
                Product prod = item.getProduct();
                prod.setQuantity(prod.getQuantity() + item.getQuantity());
            }
        }

        rp.delete(userToBeDeleted);
    }

    public List<UserResponse> findAll()
    {
        List<UserResponse> listOfUsers = new ArrayList<>();
        List<User> fullData = rp.findAll();

        for (User u : fullData)
        {
            UserResponse x = new UserResponse();
            x.setName(u.getName());
            x.setUsername(u.getUsername());

            // this is because the master has "null" as his cart
            if (u.getCart() == null)
            {
                CartResponse cr = new CartResponse();
                cr.setCartTotal(BigDecimal.ZERO);
                cr.setItem_Quantity(new HashMap<>());
                x.setCartResponse(cr);
            }
            else
                x.setCartResponse(cartService.createCartResponse(u.getCart()));

            x.setOrderResponses(orderService.ordersToResponses(u.getOrders()));
            x.setRole(u.getRole());
            listOfUsers.add(x);
        }
        return listOfUsers;
    }

    public void deleteAll()
    {
        rp.deleteAll();
    }

    public UserResponse find(String username)
    {
        User u = rp.findByUsername(username);
        UserResponse x = new UserResponse();
        x.setName(u.getName());
        x.setUsername(u.getUsername());
        x.setCartResponse(cartService.createCartResponse(u.getCart()));
        x.setOrderResponses(orderService.ordersToResponses(u.getOrders()));
        x.setRole(u.getRole());
        return x;
    }
}
