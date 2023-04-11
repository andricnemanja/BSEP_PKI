package com.example.demo.service;

import com.example.demo.dto.CertificateParamsDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public User save(User user){
       return userRepo.save(user);
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User findById(String Id){
        return userRepo.findById(Id);
    }
}
