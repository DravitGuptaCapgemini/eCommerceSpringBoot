package com.example.ecomProject.exceptions;

public class OrderDoesntExistException extends RuntimeException
{
    public OrderDoesntExistException()
    {
        super("Can't delete. Order with this ID doesn't exist.");
    }
}
