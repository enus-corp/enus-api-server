package com.enus.newsletter.db.repository.imp;

import com.enus.newsletter.db.entity.UserEntity;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    UserEntity findById(long id);
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(@Email String email);
}
