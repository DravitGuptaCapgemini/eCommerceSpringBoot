package com.example.ecomProject.repo;

import com.example.ecomProject.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer>
{
    List<Product> findByCategory(String category);
}
