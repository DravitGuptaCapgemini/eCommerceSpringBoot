package com.example.ecomProject.exceptions;

public class EmptyCartException extends RuntimeException
{
    public EmptyCartException()
    {
        super("Can't place an order, the cart is currently empty.");
    }
}
