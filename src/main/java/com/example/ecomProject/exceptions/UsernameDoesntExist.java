package com.example.ecomProject.exceptions;

public class UsernameDoesntExist extends RuntimeException
{
    public UsernameDoesntExist()
    {
        super("Cannot delete. This username doesn't exist.");
    }
}
