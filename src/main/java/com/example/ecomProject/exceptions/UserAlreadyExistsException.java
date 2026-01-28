package com.example.ecomProject.exceptions;

public class UserAlreadyExistsException extends RuntimeException
{
    public UserAlreadyExistsException(String username)
    {
        super(username + " - This username already exists.");
    }
}
