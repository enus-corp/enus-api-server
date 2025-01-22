package com.enus.newsletter.db.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="user_keywords")
@Data
public class UserKeywordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "keyword_id")
    private KeywordEntity keyword;

    @Column(nullable = false)
    private LocalDateTime dateAdded;

    @Column(name="notification_enabled")
    private boolean notificationEnabled = true;


}
