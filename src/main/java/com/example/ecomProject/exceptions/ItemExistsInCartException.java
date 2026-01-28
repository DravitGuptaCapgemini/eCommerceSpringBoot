package com.example.ecomProject.exceptions;

public class ItemExistsInCartException extends RuntimeException
{
    public ItemExistsInCartException()
    {
        super("This item already exists inside your cart, update or delete it from \"Your Cart\" page.");
    }
}
