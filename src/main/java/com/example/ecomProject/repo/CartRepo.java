package com.example.ecomProject.repo;

import com.example.ecomProject.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Integer>
{
    public Cart findByUserId(int userId);
}
