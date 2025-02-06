package com.enus.newsletter.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.enus.newsletter.db.entity.KeywordEntity;
import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.db.repository.KeywordRepository;
import com.enus.newsletter.db.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j(topic = "KEYWORD_SERVICE")
public class KeywordService {

    private final UserRepository userRepository;
    private final KeywordRepository keywordRepository;

    public KeywordService(UserRepository userRepository, KeywordRepository keywordRepository) {
        this.userRepository = userRepository;
        this.keywordRepository = keywordRepository;
    }

    public void addKeyword(Long userId, String word) {
        log.info("Adding keyword: {} for user with id: {}", word, userId);
        UserEntity user = userRepository.findUserById(userId);
        Optional<KeywordEntity> keyword = keywordRepository.findByWord(word);
                // .orElseGet(() -> keywordRepository.save(new KeywordEntity(word)));

    }

    public void getUserKeywords(Long userId) {
        log.info("Getting keywords for user with id: {}", userId);
    }
    
}
