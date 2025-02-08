package com.enus.newsletter.service;

import com.enus.newsletter.db.entity.KeywordEntity;
import com.enus.newsletter.db.entity.UserEntity;
import org.springframework.stereotype.Service;

import com.enus.newsletter.db.repository.KeywordRepository;
import com.enus.newsletter.db.repository.UserRepository;
import com.enus.newsletter.model.request.SaveKeywordEntity;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "KEYWORD_SERVICE")
public class KeywordService{

    private final UserRepository userRepository;
    private final KeywordRepository keywordRepository;

    public KeywordService(UserRepository userRepository, KeywordRepository keywordRepository) {
        this.userRepository = userRepository;
        this.keywordRepository = keywordRepository;
    }

    public List<String> addKeyword(SaveKeywordEntity req) {
        log.info("Adding keywords for user with id: {}", req.getUserId());

        // get user. throw exception if not found
        UserEntity user = userRepository.findUserById(req.getUserId());

        Set<String> newKeywords = new HashSet<>(Arrays.asList(req.getKeywords()));

         // SELECT * FROM keyword_entity WHERE word IN (newKeywords)
        Set<String> existingKeywords = keywordRepository.findByWordIn(newKeywords)
                .stream()
                .map(KeywordEntity::getWord)
                .collect(Collectors.toSet());

        // remove existing keywords from new keywords
        newKeywords.removeAll(existingKeywords);

        List<KeywordEntity> keywords = newKeywords.stream()
                .map(word -> {
                    KeywordEntity keyword = new KeywordEntity();
                    keyword.setWord(word);
                    return keywordRepository.save(keyword);
                })
                .toList();

        return keywords.stream().map(KeywordEntity::getWord).toList();

    }

    public void getUserKeywords(Long userId) {
        log.info("Getting keywords for user with id: {}", userId);
    }
    
}
