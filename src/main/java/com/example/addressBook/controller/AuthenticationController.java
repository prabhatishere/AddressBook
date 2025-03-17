package com.example.addressBook.controller;

import com.example.addressBook.dto.AuthUserDTO;
import com.example.addressBook.dto.LoginDTO;
import com.example.addressBook.model.AuthUser;
import com.example.addressBook.repository.AuthUserRepository;
import com.example.addressBook.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;
    private AuthUserRepository authUserRepository;

    @PostMapping("/register")
    public String register(@RequestBody AuthUserDTO userDTO) {
        return authenticationService.register(userDTO);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO) {
        return authenticationService.login(loginDTO).toString();
    }
    @GetMapping("/users")
    public List<AuthUser> getAllUsers() {
        return authUserRepository.findAll();
    }

}
