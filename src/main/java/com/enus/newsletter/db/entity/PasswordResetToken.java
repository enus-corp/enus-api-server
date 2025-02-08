package com.enus.newsletter.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PasswordResetToken")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, name = "response_code")
    private String responseCode;

    @Column(nullable = false, name = "email_code")
    private String emailCode;

    @Column(nullable = false, name = "expires_at")
    private LocalDateTime expiresAt;
}
