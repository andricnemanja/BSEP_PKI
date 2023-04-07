package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.demo.model.User;


public interface UserRepo extends JpaRepository<User, Integer> {
    User findByEmail(String email);

}
