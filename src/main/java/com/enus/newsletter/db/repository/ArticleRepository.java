package com.enus.newsletter.db.repository;

import com.enus.newsletter.db.AbsBaseRepository;
import com.enus.newsletter.db.entity.ArticleEntity;
import com.enus.newsletter.db.repository.imp.IArticleRepository;

public class ArticleRepository extends AbsBaseRepository<ArticleEntity, IArticleRepository> {

    public ArticleRepository(IArticleRepository repository) {
        super(repository);
    }
}
