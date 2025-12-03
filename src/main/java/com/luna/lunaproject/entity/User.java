package com.luna.lunaproject.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @Column(name = "nickName", length = 100)
    private String nickName;

    @Column(name = "passwordHash")
    private String passwordHash;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "creationDate")
    private LocalDate creationDate;

    public User() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDate creationDate) { this.creationDate = creationDate; }

    @PrePersist
    public void prePersist() {
        if (this.creationDate == null) {
            this.creationDate = LocalDate.now();
        }
    }
}