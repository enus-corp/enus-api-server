package com.enus.newsletter.db.repository.imp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enus.newsletter.db.entity.OldPasswordHistoryEntity;

@Repository
public interface IOldPasswordHistoryRepository extends JpaRepository<OldPasswordHistoryEntity, Long> {
    public boolean existsByUserIdAndEncodedPassword(Long userId, String encodedPassword);
    
}
