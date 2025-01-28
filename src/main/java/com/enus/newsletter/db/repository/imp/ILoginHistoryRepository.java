package com.enus.newsletter.db.repository.imp;

import com.enus.newsletter.db.entity.LoginHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILoginHistoryRepository extends JpaRepository<LoginHistoryEntity, Long> {
}
