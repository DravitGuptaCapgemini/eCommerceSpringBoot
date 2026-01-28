package com.example.ecomProject.services;

import com.example.ecomProject.exceptions.EmptyCartException;
import com.example.ecomProject.exceptions.OrderDoesntExistException;
import com.example.ecomProject.models.*;
import com.example.ecomProject.models.dtos.OrderResponse;
import com.example.ecomProject.repo.CartRepo;
import com.example.ecomProject.repo.OrderItemRepo;
import com.example.ecomProject.repo.OrderRepo;
import com.example.ecomProject.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService
{
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    private OrderItem cartItemToOrderItem(CartItem item, Order order)
    {
        OrderItem ordItem = new OrderItem();

        ordItem.setOrder(order);
        ordItem.setProduct(item.getProduct());
        ordItem.setQuantity(item.getQuantity());

        orderItemRepo.save(ordItem);

        return ordItem;
    }

    private Order createOrder(Cart cart)
    {
        Order newOrder = new Order();
        List<OrderItem> orderItems = new ArrayList<>();

        newOrder.setTotalPrice(cart.getTotalPrice());
        newOrder.setUser(cart.getUser());
        newOrder.setOrderItems(orderItems);

        orderRepo.save(newOrder);

        List<CartItem> items = cart.getCartItems();
        for (CartItem item : items)
        {
            orderItems.add(cartItemToOrderItem(item, newOrder));
        }
        return newOrder;
    }

    public List<OrderResponse> ordersToResponses(List<Order> orders)
    {
        List<OrderResponse> x = new ArrayList<>();
        for (Order o : orders)
        {
            OrderResponse resp = new OrderResponse();
            resp.setOrderId(o.getOrderId());
            resp.setTotalPrice(o.getTotalPrice());

            List<OrderItem> items = o.getOrderItems();
            for (OrderItem item : items)
            {
                resp.getProduct_Quantity().put(item.getProduct().getName(), item.getQuantity());
            }

            x.add(resp);
        }
        return x;
    }

    public List<OrderResponse> placeOrder(String username)
    {
        User user = userRepo.findByUsername(username);
        Cart userCart = user.getCart();

        if (userCart.getCartItems().isEmpty())
            throw new EmptyCartException();

        Order newOrder = createOrder(userCart);
        user.getOrders().add(newOrder);

        userCart.getCartItems().clear();
        userCart.setTotalPrice(BigDecimal.ZERO);

        cartRepo.save(userCart);

        List<Order> ordersOfUser = user.getOrders();

        return ordersToResponses(ordersOfUser);
    }

    // TODO -> right anyone can delete ANY order, check whether the order to be deleted belongs to the requester or not, unless it's the ADMIN, ADMIN can delete ANY order
    public void deleteOrder(int orderId)
    {
        Order order = orderRepo.findById(orderId).orElse(null);

        if (order == null)
            throw new OrderDoesntExistException();

        List<OrderItem> oItems = order.getOrderItems();

        // updating the stock quantity of the deleted products
        for (OrderItem item : oItems)
        {
            Product prod = item.getProduct();
            prod.setQuantity(prod.getQuantity() + item.getQuantity());
        }

        orderRepo.delete(order);
    }

    public List<OrderResponse> getOrders(String username)
    {
        User user = userRepo.findByUsername(username);
        List<Order> orders = user.getOrders();
        return ordersToResponses(orders);
    }
}
