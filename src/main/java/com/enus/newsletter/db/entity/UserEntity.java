package com.enus.newsletter.db.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.enus.newsletter.enums.Gender;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_username", columnList = "username"),
        @Index(name = "idx_email", columnList = "email")
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserEntity extends BaseEntity {

    public UserEntity(@NotBlank @Email String email) {
        this.email = email;
        this.isOauthUser = true;
    }

    public UserEntity(
            @NotBlank @Size(max = 50) String firstName,
            @NotBlank @Size(max = 50) String lastName,
            @NotBlank @Size(max = 50) String username,
            @NotBlank @Size(max = 100) String password,
            @NotBlank @Size(max = 100) @Email String email,
            @NotBlank Gender gender,
            @NotBlank int age,
            boolean isOauthUser
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.isOauthUser = isOauthUser;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = true, length=20, name = "first_name")
    private String firstName;

    @Column(nullable = true, length=20, name = "last_name")
    private String lastName;

    @Column(nullable = true, unique = true, length=255)
    private String username;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false, length = 100)
    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, name = "gender", columnDefinition = "VARCHAR(10)")
    private Gender gender;

    @Column(nullable = true)
    private int age;

    @Column(nullable = false, name = "is_oauth_user")
    private boolean isOauthUser;

    @Column(nullable = false, name = "is_expired", columnDefinition = "smallint default 0")
    private short isExpired;

    @Column(nullable = false, name = "is_locked", columnDefinition = "smallint default 0")
    private short isLocked;

    @Column(nullable = false, name = "attempt", columnDefinition = "smallint default 0 CHECK (attempt >= 0 AND attempt <= 5)")
    private short attempt;

    @OneToMany(mappedBy = "user")
    private Set<UserKeywordEntity> keyword;

    @Column(nullable = true, name = "last_attempt_at")
    private LocalDateTime lastAttemptAt;

    @Column(nullable = true, name = "provider")
    private String provider;

    @Column(nullable = true, name = "provider_id")
    private String providerId;

    @Column(nullable = false, name = "is_email_updated", columnDefinition = "BOOLEAN default false")
    private boolean isEmailUpdated;

    @OneToMany(mappedBy ="user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChatSessionEntity> chatSessions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PasswordHistoryEntity> passwordHistories = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PasswordResetTokenEntity> passwordResetTokens = new ArrayList<>();

//    @ElementCollection
//    @CollectionTable(name="roles", joinColumns = @JoinColumn(name="user_id", nullable = false))
//    @Column(name= "role", nullable = false, length = 50)
//    private List<String> hasRole = new ArrayList<>();

    public void handleLoginAttempt(boolean isSuccessful) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (!isSuccessful) {
            if (isLocked == 1) {
                return;
            }

            if (lastAttemptAt != null && lastAttemptAt.plusMinutes(30).isBefore(currentTime)) {
                // reset counter to 1 if last attempt was more than 30 minutes ago
                attempt = 1;
            } else if (attempt >= 4) {
                // lock account
                isLocked = 1;
            } else {
                // Not locked, increment login attempts
                attempt++;
            }
        } else {
            // reset login attempts
            attempt = 0;
            lastAttemptAt = null;
        }
    }

    public void resetPassword(String newPassword) {
        this.password = newPassword;
    }

}
