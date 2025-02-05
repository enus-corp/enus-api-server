package com.enus.newsletter.db.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users", indexes = {
        @Index(name = "idx_username", columnList = "username"),
        @Index(name = "idx_email", columnList = "email"),
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseEntity implements UserDetails {
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
    private short isExpired;

    @Column(nullable = false, name = "is_locked", columnDefinition = "smallint default 0")
    private short isLocked;

    @Column(nullable = false, name = "attempt", columnDefinition = "smallint default 0 CHECK (attempt >= 0 AND attempt <= 5)")
    private short attempt;

    @OneToMany(mappedBy = "user")
    private Set<UserKeywordEntity> keyword;

    @Column(nullable = true, name = "last_attempt_at")
    private LocalDateTime lastAttemptAt;

    @ElementCollection
    @CollectionTable(name="roles", joinColumns = @JoinColumn(name="user_id", nullable = false))
    @Column(name= "role", nullable = false, length = 50)
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

}
