package com.example.addressBook.service;

import com.example.addressBook.dto.AuthUserDTO;
import com.example.addressBook.dto.LoginDTO;
import com.example.addressBook.model.AuthUser;
import com.example.addressBook.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    @Autowired
    private AuthUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenService jwtTokenService;

    public String register(AuthUserDTO userDTO) {
        // Check if email already exists
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use");
        }

        // Ensure password is not null
        if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be empty");
        }

        // Create new user object
        AuthUser user = new AuthUser();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());

        // Encode password before saving to database
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setHashPass(encodedPassword);

        userRepository.save(user);
        return "User registered successfully!";
    }


    public Map<String, String> login(LoginDTO loginDTO) {
        AuthUser user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (passwordEncoder.matches(loginDTO.getPassword(), user.getHashPass())) {
            String token = jwtTokenService.createToken(user.getId());

            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", token);
            return response;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

}
