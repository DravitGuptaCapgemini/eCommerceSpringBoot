package com.example.ecomProject.controllers;

import com.example.ecomProject.models.Product;
import com.example.ecomProject.models.dtos.CreateProductRequest;
import com.example.ecomProject.models.dtos.ProductResponse;
import com.example.ecomProject.models.dtos.UpdateProductRequest;
import com.example.ecomProject.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/product")
public class ProductController
{
    @Autowired
    private ProductService srvce;

    @GetMapping("/all")
    public ResponseEntity<?> getAllProducts()
    {
        List<ProductResponse> products = srvce.findAll();
        return new ResponseEntity<>(
                products,
                HttpStatus.OK
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductbyID(@PathVariable int productId)
    {
        ProductResponse product = srvce.findById(productId);
        return new ResponseEntity<>(
                product,
                HttpStatus.OK
        );
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable String category)
    {
        List<ProductResponse> productResponses = srvce.findByCategory(category);
        return new ResponseEntity<>(
                productResponses,
                HttpStatus.OK
        );
    }


    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@Valid @RequestBody CreateProductRequest prod)
    {
        ProductResponse savedProduct = srvce.save(prod);
        // will check whether another product with the same name exists or not afterward
        return new ResponseEntity<>(
                savedProduct,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody UpdateProductRequest pd, @PathVariable int productId)
    {
        ProductResponse updatedProduct = srvce.update(
                productId,
                pd
        );
        return new ResponseEntity<>(
                updatedProduct,
                HttpStatus.OK
        );
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable int productId)
    {
        srvce.delete(productId); return new ResponseEntity<>(
            "Product with ID = " + productId + " deleted.",
            HttpStatus.OK
        );
    }
}
