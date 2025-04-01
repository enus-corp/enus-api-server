package com.enus.newsletter.db.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="user_keywords")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserKeywordEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "keyword_id", nullable = false)
    private KeywordEntity keyword;

    @Column(name="notification_enabled")
    private boolean notificationEnabled = true;


}
