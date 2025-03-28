package com.enus.newsletter.db.repository.imp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enus.newsletter.db.entity.PasswordHistoryEntity;

@Repository
public interface IPasswordHistoryRepository extends JpaRepository<PasswordHistoryEntity, Long> {
    public boolean existsByUserIdAndPassword(Long userId, String password);
    
}
