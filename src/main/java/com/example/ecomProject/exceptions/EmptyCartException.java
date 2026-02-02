package com.example.ecomProject.exceptions;

public class EmptyCartException extends RuntimeException
{
    public EmptyCartException()
    {
        super("Can't do that, the cart is currently empty.");
    }
}
