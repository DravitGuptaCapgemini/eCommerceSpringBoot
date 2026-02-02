package com.example.ecomProject.exceptions;

public class NoRecommendationsException extends RuntimeException
{
    public NoRecommendationsException()
    {
        super("No recommendations for you.");
    }
}
