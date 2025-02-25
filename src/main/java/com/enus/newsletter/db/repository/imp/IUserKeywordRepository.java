package com.enus.newsletter.db.repository.imp;

import com.enus.newsletter.db.entity.KeywordEntity;
import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.db.entity.UserKeywordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUserKeywordRepository extends JpaRepository<UserKeywordEntity, Long> {
    List<UserKeywordEntity> findByUser(UserEntity user);
    Optional<UserKeywordEntity> findByUserAndKeyword(UserEntity user, KeywordEntity keyword);
    void deleteByUserAndKeyword(UserEntity user, KeywordEntity keyword);
    boolean existsByUserAndKeyword(UserEntity user, KeywordEntity keyword);
}
