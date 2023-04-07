package com.example.demo.controller;

import com.example.demo.dto.CertificateParamsDTO;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/CertificateController")
public class CertificateController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/generateCertificate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity generateCertificate(@RequestBody CertificateParamsDTO certificateParamsDTO) {
            User user =userService.findByEmail(certificateParamsDTO.email);
            if(user == null) {
                UUID uuid = UUID.randomUUID();
                String uuidString = uuid.toString();

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String hashedPassword = passwordEncoder.encode(certificateParamsDTO.password);

                user = new User(uuidString, certificateParamsDTO.commonName, certificateParamsDTO.surname, certificateParamsDTO.givenName, certificateParamsDTO.organization,
                        certificateParamsDTO.organizationUnit, certificateParamsDTO.country, certificateParamsDTO.email, hashedPassword, new ArrayList<>());
                userService.save(user);
            }

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(value = "/getall", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
