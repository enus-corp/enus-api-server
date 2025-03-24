package com.enus.newsletter.db.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @JsonIgnore
    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    @Column(nullable = true, name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @Column(nullable = true, name = "deleted_at")
    private LocalDateTime deletedAt;
}
