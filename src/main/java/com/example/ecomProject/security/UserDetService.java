package com.example.ecomProject.security;

import com.example.ecomProject.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    // This fetches the user details(username, password and role) stored inside the database
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var u = userRepo.findByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("Username doesn't exist.");
        }

        String password = u.getPassword();

        var authorities = u.getRole();

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getUsername())
                .password(password)
                .authorities(authorities)
                .build();
    }
}
