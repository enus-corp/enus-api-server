package com.enus.newsletter.db.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "login_histories")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginHistoryEntity extends BaseEntity{
    public LoginHistoryEntity(String username, String ipAddress, int loginStatus, String reason) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.loginStatus = loginStatus;
        this.reason = reason;
    }
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
