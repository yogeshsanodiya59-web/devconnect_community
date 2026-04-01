package com.devconnect.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.security.CodeSigner;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String name ;

    private String email ;

    private String avtarurl;

    @Column(unique = true)
    private String githubUsername ;

    private String githubAccessToken;

    private String leetcodeUsername ;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER ;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Role {
        USER ,ADMIN
    }


}
