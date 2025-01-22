package com.enus.newsletter.db.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="roles")
@Data
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
