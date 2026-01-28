package com.example.ecomProject.services;

import com.example.ecomProject.exceptions.ItemDoesntExistException;
import com.example.ecomProject.exceptions.ItemExistsInCartException;
import com.example.ecomProject.models.Cart;
import com.example.ecomProject.models.CartItem;
import com.example.ecomProject.models.Product;
import com.example.ecomProject.models.User;
import com.example.ecomProject.models.dtos.AddToCartRequest;
import com.example.ecomProject.models.dtos.CartResponse;
import com.example.ecomProject.repo.CartItemRepo;
import com.example.ecomProject.repo.CartRepo;
import com.example.ecomProject.repo.ProductRepo;
import com.example.ecomProject.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService
{
    @Autowired
    private CartRepo rp;

    @Autowired
    private UserRepo userRp;

    @Autowired
    private ProductRepo prodRp;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private CartRepo cartRepo;

    public void createCart(User user)
    {
        Cart newCart = new Cart();

        newCart.setUser(user);
        user.setCart(newCart);
        rp.save(newCart);
    }

    public CartResponse createCartResponse(Cart userCart)
    {
        CartResponse response = new CartResponse();
        response.setCartTotal(userCart.getTotalPrice());

        List<CartItem> items = userCart.getCartItems();
        for (CartItem item : items)
        {
            response.getItem_Quantity()
                    .put(
                            item.getProduct()
                                .getName(),
                            item.getQuantity()
                    );
        }

        return response;
    }

    private void calculateCartTotal(Cart userCart)
    {
        BigDecimal total = BigDecimal.ZERO;
        List<CartItem> items = userCart.getCartItems();

        for (CartItem item : items)
        {
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            BigDecimal price = item.getProduct()
                                   .getPrice();
            BigDecimal cartItemPrice = quantity.multiply(price);
            total = total.add(cartItemPrice);
        }

        userCart.setTotalPrice(total);
    }

    public CartResponse addCartItem(Authentication auth, AddToCartRequest cartItem)
    {
        String username = auth.getName();
        User user = userRp.findByUsername(username);
        Cart userCart = user.getCart();

        for (CartItem item : userCart.getCartItems())
        {
            if (item.getProduct().getId() == cartItem.getProductId())
                throw new ItemExistsInCartException();
        }

        CartItem itemToBeAdded = new CartItem();

        itemToBeAdded.setCart(userCart);
        itemToBeAdded.setQuantity(cartItem.getQuantity());
        itemToBeAdded.setProduct(prodRp.findById(cartItem.getProductId())
                                       .orElse(null));

        if (itemToBeAdded.getProduct() == null)
            throw new ItemDoesntExistException();

        userCart.getCartItems()
                .add(itemToBeAdded);
        calculateCartTotal(userCart);

        cartItemRepo.save(itemToBeAdded);
        Product product = prodRp.findById(cartItem.getProductId())
                                .orElse(null);
        product.setQuantity(product.getQuantity() - cartItem.getQuantity());
        prodRp.save(product);
        return createCartResponse(userCart);
    }

    public CartResponse emptyTheCart(Authentication auth)
    {
        String username = auth.getName();
        User user = userRp.findByUsername(username);

        Cart userCart = user.getCart();
        // make userCart.TotalPrice = 0
        // empty the userCart.CartItems
        // delete all CartItems from the table
        // update the product stock quantity

        userCart.setTotalPrice(BigDecimal.ZERO);

        List<CartItem> items = userCart.getCartItems();

        for (CartItem item : items)
        {
            Product updated = item.getProduct();
            updated.setQuantity(updated.getQuantity() + item.getQuantity());
            prodRp.save(updated);
        }

        userCart.getCartItems()
                .clear();

        cartRepo.save(userCart);

        return createCartResponse(userCart);
    }

    public CartResponse deleteCartItem(Authentication auth, int productId)
    {
        String username = auth.getName();
        User user = userRp.findByUsername(username);
        Cart userCart = user.getCart();
        List<CartItem> items = userCart.getCartItems();
        // delete from cartItems table
        // update the product stock
        // update the cart.totalPrice

        CartItem itemToBeRemoved = null;
        for (CartItem item : items)
        {
            if (item.getProduct()
                    .getId() == productId)
            {
                itemToBeRemoved = item;

                Product updated = item.getProduct();
                updated.setQuantity(updated.getQuantity() + item.getQuantity());
                prodRp.save(updated);

                BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
                BigDecimal price = item.getProduct()
                                       .getPrice();
                BigDecimal itemPrice = quantity.multiply(price);

                userCart.setTotalPrice(userCart.getTotalPrice()
                                               .subtract(itemPrice));

                break;
            }
        }

        items.remove(itemToBeRemoved);

        cartRepo.save(userCart);
        return createCartResponse(userCart);
    }
}
