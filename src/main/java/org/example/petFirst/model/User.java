package org.example.petFirst.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String passwordHash;
    private String refreshToken;
    @Enumerated(EnumType.STRING)
    private Role role;
}
