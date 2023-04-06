package com.example.demo.controller;

import com.example.demo.dto.CertificateParamsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/CertificateController")
public class CertificateConteoller {

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/generateCertificate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity generateCertificate(@RequestBody CertificateParamsDTO certificateParamsDTO) {
        System.out.println(certificateParamsDTO);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value="/getall", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllUsers() {
        System.out.println("**********************************************");
        return new ResponseEntity<>("nestoo", HttpStatus.OK);
    }

}
