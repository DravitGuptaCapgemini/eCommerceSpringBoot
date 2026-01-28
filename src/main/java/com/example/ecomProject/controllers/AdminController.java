package com.example.ecomProject.controllers;

import com.example.ecomProject.models.dtos.UserRequest;
import com.example.ecomProject.models.dtos.UserResponse;
import com.example.ecomProject.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController
{
    @Autowired
    private UserService srvce;

    @PostMapping("/create")
    public ResponseEntity<?> createAdminUser(@Valid @RequestBody UserRequest user)
    {
        UserResponse savedAdmin = srvce.saveAdmin(user);
        return new ResponseEntity<>(
                savedAdmin,
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/deleteUser/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username)
    {
        srvce.deleteUser(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers()
    {
        List<UserResponse> users = srvce.findAll();
        return new ResponseEntity<>(
                users,
                HttpStatus.OK
        );
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<?> deleteUser()
    {
        srvce.deleteAll();
        return new ResponseEntity<>(
                "Deleted all users.",
                HttpStatus.OK
        );
    }
}
