package com.enus.newsletter.db.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "password_reset_tokens")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PasswordResetTokenEntity {
    public PasswordResetTokenEntity(UserEntity user, String code, LocalDateTime expiresAt) {
        this.user = user;
        this.code = code;
        this.expiresAt = expiresAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, name = "email_code")
    private String code;

    @Column(nullable = false, name = "is_active", columnDefinition = "boolean default true")
    private boolean isActive = true;

    @Column(nullable = false, name = "expires_at")
    private LocalDateTime expiresAt;
}
