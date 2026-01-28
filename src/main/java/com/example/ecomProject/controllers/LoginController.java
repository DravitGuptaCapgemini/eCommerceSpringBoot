package com.example.ecomProject.controllers;

import com.example.ecomProject.models.dtos.LoginRequest;
import com.example.ecomProject.security.JwtUtils;
import com.example.ecomProject.security.UserDetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController
{
    // AuthenticationManager is autoconfigured by spring, but it cannot be used as a bean unless you explicitly define it in the security configuration
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetService userDetService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping()
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest user)
    {
        String enteredUsername = user.getUsername();
        String enteredPassword = user.getPassword();

        // this can throw an unchecked exception
        // we are identifying/verifying the identity of the user
        // throws an exception if the credentials are wrong
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                enteredUsername,
                enteredPassword
        ));

        // control of the program reaches here if no exception occurs, and the user is already authenticated
        UserDetails userDetails = userDetService.loadUserByUsername(enteredUsername);
        String jwtToken = jwtUtils.generateToken(enteredUsername);
        return new ResponseEntity<>(
                jwtToken,
                HttpStatus.OK
        );
    }
}
