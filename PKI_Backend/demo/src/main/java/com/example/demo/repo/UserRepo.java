package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.demo.model.User;
import org.springframework.data.repository.query.Param;


public interface UserRepo extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    @Query(value="select * from users u where u.id = :id",nativeQuery = true)
    User findById(@Param(value = "id") String id);
}
