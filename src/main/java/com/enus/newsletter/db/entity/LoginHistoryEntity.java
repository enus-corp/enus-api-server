package com.enus.newsletter.db.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "login_histories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginHistoryEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, name = "ip_address")
    private String ipAddress;

    @Column(nullable = false, name = "login_status")
    private int loginStatus;

    @Column(nullable = false)
    private String reason;

}
