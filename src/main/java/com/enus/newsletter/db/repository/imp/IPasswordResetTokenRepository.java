package com.enus.newsletter.db.repository.imp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.enus.newsletter.db.entity.PasswordResetTokenEntity;

@Repository
public interface IPasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {
    Optional<List<PasswordResetTokenEntity>> findByUserId(Long userId);
    
    @Query(
        """
        SELECT prt 
        FROM PasswordResetTokenEntity prt 
        WHERE prt.code = :code AND prt.expiresAt > :now                
        """
    )
    Optional<PasswordResetTokenEntity> findValidTokenByEmailCode(
            String code, LocalDateTime now
    );
}
