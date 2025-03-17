package com.example.addressBook.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "auth_user") // Ensure correct table name
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Ensure ID is auto-increment
    private Long id;

    private String name;

    @Column(unique = true, nullable = false) // Ensure email is unique and required
    private String email;

    private String phone;

    @Column(name = "password", nullable = false) // Explicitly map hashPass to 'password' column
    private String hashPass;

    private String token; // JWT or role information
}
