package com.example.ecomProject.config;

import com.example.ecomProject.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration
{
    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http
                .csrf(csrf -> csrf.disable())   // disable CSRF for APIs
                .formLogin(form -> form.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/cart/**").authenticated()

                        .requestMatchers(
                                "/product/create",
                                "/product/update/**",
                                "/product/delete/**"
                        )
                        .hasAuthority("ADMIN")

                        .requestMatchers(
                                "/admin/**"
                        )
                        .hasAuthority("ADMIN")

                        .requestMatchers("/user").authenticated()

                        .requestMatchers("/order/**").authenticated()

                        .anyRequest()
                        .permitAll()
                )
                // UsernamePasswordAuthenticationFilter.class is the default Form based authentication
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        // return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    // we use this bean inside the login controller, to authenticate the user
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

}
