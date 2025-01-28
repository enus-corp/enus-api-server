package com.enus.newsletter.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity(name = "login_history")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String ipAddress;

    @Column(nullable = false)
    private int loginStatus;

    @Column(nullable = false)
    private String reason;

    @CreationTimestamp
    @Column(nullable = false, name = "create_at")
    @Builder.Default
    private LocalDateTime createAt = LocalDateTime.now();

}
