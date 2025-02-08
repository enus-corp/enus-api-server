package com.enus.newsletter.db.repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;

import com.enus.newsletter.db.entity.KeywordEntity;
import com.enus.newsletter.db.repository.imp.IKeywordRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "KEYWORD_REPOSITORY")
@Repository
@Transactional
public class KeywordRepository {
    public final IKeywordRepository keywordRepository;

    public KeywordRepository(IKeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    public Optional<KeywordEntity> findByWord(String word) {
        return keywordRepository.findByWord(word);
    }

    public Set<KeywordEntity> findByWordIn(Iterable<String> words) {
        return keywordRepository.findByWordIn(words);
    }
}
