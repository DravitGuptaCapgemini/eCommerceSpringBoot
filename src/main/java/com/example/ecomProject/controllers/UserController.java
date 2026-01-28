package com.example.ecomProject.controllers;

import com.example.ecomProject.models.dtos.UserRequest;
import com.example.ecomProject.models.dtos.UserResponse;
import com.example.ecomProject.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController
{
    @Autowired
    private UserService srvce;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest user)
    {
        UserResponse savedUser = srvce.save(user);
        return new ResponseEntity<>(
                savedUser,
                HttpStatus.CREATED
        );
    }

    @GetMapping()
    public ResponseEntity<?> getUser(Authentication auth)
    {
        String username = auth.getName();
        UserResponse userResponse = srvce.find(username);
        return new ResponseEntity<>(
                userResponse,
                HttpStatus.OK
        );
    }
}
