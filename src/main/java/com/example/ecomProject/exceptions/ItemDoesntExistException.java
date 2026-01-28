package com.example.ecomProject.exceptions;

public class ItemDoesntExistException extends RuntimeException
{
    public ItemDoesntExistException()
    {
        super("Item with this ProductID doesn't exist.");
    }
}
