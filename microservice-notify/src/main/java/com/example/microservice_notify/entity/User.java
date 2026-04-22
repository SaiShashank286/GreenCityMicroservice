package com.example.microservice_notify.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer userId;
    @Column(nullable=false)
    private String name;
    @Column(nullable=false)
    private String role;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(name="phone")
    private String phone;
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    @Column(name="status")
    private String status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AuditLog> auditLogs;

    public User(Integer userId) {
    }
}


