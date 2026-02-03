package com.example.ecomProject.repo;

import com.example.ecomProject.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer>
{
    List<Product> findByCategory(String category);
    List<Product> findByCategoryAndIdNotIn(String category, Set<Integer> ids);
}
