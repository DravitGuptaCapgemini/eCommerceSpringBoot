package com.example.ecomProject.controllers;

import com.example.ecomProject.models.dtos.CartResponse;
import com.example.ecomProject.models.dtos.UserRequest;
import com.example.ecomProject.models.dtos.UserResponse;
import com.example.ecomProject.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;


@WebMvcTest(
        controllers = UserController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        },
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = com.example.ecomProject.security.JwtFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private com.example.ecomProject.security.UserDetService userDetService;

    @Test
    void createUser_shouldValidateAndSaveTheUser() throws Exception
    {
        UserRequest user = new UserRequest();
        user.setName("X");
        user.setUsername("x");
        user.setPassword("x");

        UserResponse userResponse = new UserResponse();
        userResponse.setName("X");
        userResponse.setUsername("x");
        userResponse.setRole("USER");
        userResponse.setCartResponse(new CartResponse());
        userResponse.setOrderResponses(new ArrayList<>());

        when(userService.save(any())).thenReturn(userResponse);

        mockMvc.perform(post("/user/create")
                       .content(objectMapper.writeValueAsString(user))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.name").value("X"))
               .andExpect(jsonPath("$.username").value("x"))
               .andExpect(jsonPath("$.role").value("USER"));

        verify(userService).save(any());
    }

    @Test
    void createInvalidUser_shouldntSave() throws Exception
    {
        UserRequest user = new UserRequest();
        user.setName("");
        user.setUsername("x");
        user.setPassword("x");

        List<String> output = new ArrayList<>();
        output.add("name : must not be blank");

        // there will be no user response, the name is blank(invalid)

        mockMvc.perform(post("/user/create")
                       .content(objectMapper.writeValueAsString(user))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest());

        verify(userService, never()).save(any());
    }
}