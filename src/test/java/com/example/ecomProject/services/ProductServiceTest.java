package com.example.ecomProject.services;

import com.example.ecomProject.repo.ProductRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest
{
    @Mock
    ProductRepo productRepo;

    @InjectMocks
    ProductService productService;

    @Test
    void testAddProduct()
    {
    }
}
