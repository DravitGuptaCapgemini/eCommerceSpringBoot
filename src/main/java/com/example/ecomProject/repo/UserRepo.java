package com.example.ecomProject.repo;

import com.example.ecomProject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer>
{
    boolean existsByUsername(String username);

    User findByUsername(String username);
}
