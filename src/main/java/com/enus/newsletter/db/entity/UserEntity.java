package com.enus.newsletter.db.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name="users", indexes = {
        @Index(name = "idx_username", columnList = "username"),
        @Index(name = "idx_email", columnList = "email"),
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    @Email
    private String email;

    @Column(nullable = false, name = "is_expired", columnDefinition = "smallint default 0")
    @Builder.Default
    private short isExpired = 0;

    @Column(nullable = false, name = "is_locked", columnDefinition = "smallint default 0")
    @Builder.Default
    private short isLocked = 0;

    @Column(nullable = false, name = "attempt", columnDefinition = "smallint default 0 CHECK (attempt >= 0 AND attempt <= 5)")
    @Builder.Default
    private short attempt = 0;

    @Column(nullable = true, name = "last_attempt_at")
    private LocalDateTime lastAttemptAt;

    @Column(nullable = false, name = "is_deleted", columnDefinition = "smallint default 0")
    @Builder.Default
    private short isDeleted = 0;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "created_by")
    private Long createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "updated_by")
    @Builder.Default
    private Long updatedBy = null;

    @Column(nullable = true, name = "deleted_at")
    @Builder.Default
    private LocalDateTime deletedAt = null;

    @Column(nullable = true, name = "deleted_by")
    @Builder.Default
    private Long deletedBy = null;

    @ElementCollection
    @CollectionTable(name="roles", joinColumns = @JoinColumn(name="user_id, nullable = false"))
    @Column(name= "role", nullable = false, length = 50)
    @Builder.Default
    private List<String> hasRole = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return hasRole.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return isExpired == 0;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isLocked == 0;
    }

    @Override
    public boolean isEnabled() {
        return isDeleted == 0;
    }
}
