package com.enus.newsletter.db.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="UserKeywords")
@Data
public class UserKeywordEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "keyword_id", nullable = false)
    private KeywordEntity keyword;

    @Column(name="notification_enabled")
    private boolean notificationEnabled = true;


}
