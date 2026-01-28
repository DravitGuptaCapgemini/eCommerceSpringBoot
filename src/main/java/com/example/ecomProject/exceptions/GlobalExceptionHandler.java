package com.example.ecomProject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> invalidInput(MethodArgumentNotValidException ex)
    {
        List<FieldError> errors = ex.getBindingResult()
                                    .getFieldErrors();
        List<String> validationErrors = new ArrayList<>();
        for (FieldError fe : errors)
        {
            validationErrors.add(fe.getField() + " : " + fe.getDefaultMessage());
        }
        return new ResponseEntity<>(
                validationErrors,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> userAlreadyExists(UserAlreadyExistsException ex)
    {
        String message = ex.getMessage();
        return new ResponseEntity<>(
                message,
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(UsernameDoesntExist.class)
    public ResponseEntity<?> usernameDoesntExist(UsernameDoesntExist ex)
    {
        String message = ex.getMessage();
        return new ResponseEntity<>(
                message,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentialsException()
    {
        String message = "Wrong credentials.";
        return new ResponseEntity<>(
                message,
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(ItemExistsInCartException.class)
    public ResponseEntity<?> itemExistsInCart(ItemExistsInCartException ex)
    {
        String message = ex.getMessage();
        return new ResponseEntity<>(
                message,
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(ItemDoesntExistException.class)
    public ResponseEntity<?> itemDoesntExist(ItemDoesntExistException ex)
    {
        String message = ex.getMessage();
        return new ResponseEntity<>(
                message,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(OrderDoesntExistException.class)
    public ResponseEntity<?> orderDoesntExist(OrderDoesntExistException ex)
    {
        String message = ex.getMessage();
        return new ResponseEntity<>(
                message,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<?> emptyCart(EmptyCartException ex)
    {
        String message = ex.getMessage();
        return new ResponseEntity<>(
                message,
                HttpStatus.NOT_FOUND
        );
    }
}
