package com.enus.newsletter.db.repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import com.enus.newsletter.db.AbsBaseRepository;
import org.springframework.stereotype.Repository;

import com.enus.newsletter.db.entity.KeywordEntity;
import com.enus.newsletter.db.repository.imp.IKeywordRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "KEYWORD_REPOSITORY")
@Repository
@Transactional
public class KeywordRepository extends AbsBaseRepository<KeywordEntity, IKeywordRepository> {

    public KeywordRepository(IKeywordRepository keywordRepository) {
        super(keywordRepository);
    }

    public Optional<KeywordEntity> findByWord(String word) {
        return repository.findByWord(word);
    }

    public Set<KeywordEntity> findByWordIn(Iterable<String> words) {
        return repository.findByWordIn(words);
    }
}
