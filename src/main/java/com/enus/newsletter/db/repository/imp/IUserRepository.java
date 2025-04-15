package com.enus.newsletter.db.repository.imp;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enus.newsletter.db.entity.UserEntity;

import jakarta.validation.constraints.Email;


@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<UserEntity> findById(long id);
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(@Email String email);
    Optional<UserEntity> findByProviderId(String providerId);
}
