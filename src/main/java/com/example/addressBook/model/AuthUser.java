package com.example.addressBook.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Table(name = "auth_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;

    @Column(name = "password", nullable = false)
    private String hashPass;

    private String token; // JWT or role information

    private String resetToken; // For password reset
    private LocalDateTime resetTokenExpiry; // Expiry time for reset token
}
