package com.enus.newsletter.db.repository.imp;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enus.newsletter.db.entity.KeywordEntity;

@Repository
public interface IKeywordRepository extends JpaRepository<KeywordEntity, Long> {
    Optional<KeywordEntity> findByWord(String word);
    
}
