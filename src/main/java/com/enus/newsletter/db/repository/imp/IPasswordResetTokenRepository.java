package com.enus.newsletter.db.repository.imp;

import com.enus.newsletter.db.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface IPasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    @Query(
            "SELECT prt FROM PasswordResetToken prt WHERE prt.responseCode = :responseCode AND prt.emailCode = :emailCode AND prt.expiresAt > :now"
    )
    Optional<PasswordResetToken> findValidTokenByEmailCode(
            String responseCode, String emailCode, LocalDateTime now
    );
}
