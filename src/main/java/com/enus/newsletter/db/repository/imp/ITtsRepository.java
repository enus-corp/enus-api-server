package com.enus.newsletter.db.repository.imp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enus.newsletter.db.entity.TtsEntity;

@Repository
public interface ITtsRepository extends JpaRepository<TtsEntity, Long> {
    
}
