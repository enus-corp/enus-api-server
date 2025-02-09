package com.enus.newsletter.db.repository.imp;

import com.enus.newsletter.db.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IArticleRepository extends JpaRepository<ArticleEntity, Long> {
}
