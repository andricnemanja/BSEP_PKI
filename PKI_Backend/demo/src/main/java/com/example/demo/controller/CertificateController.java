package com.example.demo.controller;

import com.example.demo.dto.CertificateParamsDTO;
import com.example.demo.model.User;
import com.example.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/CertificateController")
public class CertificateController {
    @Autowired
    private UserRepo userRepo;

    @PostMapping(value = "/generateCertificate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity generateCertificate(@RequestBody CertificateParamsDTO certificateParamsDTO) {
            User user =userRepo.findByEmail(certificateParamsDTO.email);
            if(user == null) {
                UUID uuid = UUID.randomUUID();
                String uuidString = uuid.toString();
                user = new User(uuidString, certificateParamsDTO.commonName, certificateParamsDTO.surname, certificateParamsDTO.givenName, certificateParamsDTO.organization,
                        certificateParamsDTO.organizationUnit, certificateParamsDTO.country, certificateParamsDTO.email, certificateParamsDTO.password, new ArrayList<>());
                userRepo.save(user);
            }

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(value = "/getall", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
