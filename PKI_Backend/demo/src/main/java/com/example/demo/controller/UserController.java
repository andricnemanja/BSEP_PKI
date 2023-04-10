package com.example.demo.controller;

import com.example.demo.dto.CertificateParamsDTO;
import com.example.demo.dto.CredentialsDTO;
import com.example.demo.dto.LoginDTO;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/UserController")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CredentialsDTO> login(@RequestBody LoginDTO loginDTO) {
        User user = userService.findByEmail(loginDTO.email);
        if(user == null){
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(passwordEncoder.matches(loginDTO.password, user.getPassword())){
            CredentialsDTO credentialsDTO = new CredentialsDTO(user.getId(), user.getRole());
            return new ResponseEntity<CredentialsDTO>(credentialsDTO, HttpStatus.OK);
        }

        return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity logout(){
        return new ResponseEntity(HttpStatus.OK);
    }
}
