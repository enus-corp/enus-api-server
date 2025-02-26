package com.enus.newsletter.service;

import com.enus.newsletter.db.entity.KeywordEntity;
import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.db.entity.UserKeywordEntity;
import com.enus.newsletter.db.repository.imp.IUserKeywordRepository;
import com.enus.newsletter.exception.keyword.KeywordErrorCode;
import com.enus.newsletter.exception.keyword.KeywordException;
import com.enus.newsletter.model.dto.KeywordDTO;
import com.enus.newsletter.model.dto.UserDTO;
import com.enus.newsletter.model.request.keyword.BatchKeywordRequest;
import com.enus.newsletter.model.request.keyword.KeywordRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.enus.newsletter.db.repository.KeywordRepository;
import com.enus.newsletter.db.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "KEYWORD_SERVICE")
public class KeywordService{

    private final IUserKeywordRepository userKeywordRepository;
    private final UserRepository userRepository;
    private final KeywordRepository keywordRepository;

    public KeywordService(IUserKeywordRepository userKeywordRepository, UserRepository userRepository, KeywordRepository keywordRepository) {
        this.userKeywordRepository = userKeywordRepository;
        this.userRepository = userRepository;
        this.keywordRepository = keywordRepository;
    }

    public List<KeywordDTO> getUserKeywords(Long userId) {
        UserEntity user = userRepository.findUserById(userId);

        return userKeywordRepository.findByUser(user).stream()
                .map(uk -> KeywordDTO.builder()
                        .id(uk.getKeyword().getId())
                        .word(uk.getKeyword().getWord())
                        .notificationEnabled(uk.isNotificationEnabled())
                        .build()).collect(Collectors.toList());

    }

    @Transactional
    public KeywordDTO addKeywordToUser(Long userId, KeywordRequest req) {
        log.info("Adding Keyword for user : {}", userId);

        UserEntity user = userRepository.findUserById(userId);

        KeywordEntity keyword = keywordRepository.findByWord(req.getWord())
                .orElseGet(() -> {
                    KeywordEntity newKeyword = new KeywordEntity();
                    newKeyword.setWord(req.getWord());
                    return keywordRepository.save(newKeyword);
                });

        if (userKeywordRepository.existsByUserAndKeyword(user, keyword)) {
            log.info("Keyword already exists for user");
            throw new KeywordException(KeywordErrorCode.KEYWORD_ALREADY_EXISTS, KeywordErrorCode.KEYWORD_ALREADY_EXISTS.getMessage());
        }

        UserKeywordEntity userKeyword  = new UserKeywordEntity();
        userKeyword.setUser(user);
        userKeyword.setKeyword(keyword);
        userKeyword.setNotificationEnabled(req.isNotificationEnabled());
        userKeywordRepository.save(userKeyword);

        return KeywordDTO.builder()
                .id(keyword.getId())
                .word(keyword.getWord())
                .notificationEnabled(req.isNotificationEnabled())
                .build();
    }

    @Transactional
    public List<KeywordDTO> addKeywordsInBatch(Long userId, List<KeywordRequest> keywords) {
        log.info("Adding Keywords in batch for user : {}", userId);

        UserEntity user = userRepository.findUserById(userId);

        return keywords.stream()
                .map(k -> {
                    KeywordEntity keyword = keywordRepository.findByWord(k.getWord())
                            .orElseGet(() -> {
                                KeywordEntity newKeyword = new KeywordEntity();
                                newKeyword.setWord(k.getWord());
                                return keywordRepository.save(newKeyword);
                            });

                    // skip if user already has the keyword
                    if (!userKeywordRepository.existsByUserAndKeyword(user, keyword)) {
                        UserKeywordEntity userKeyword = new UserKeywordEntity();
                        userKeyword.setUser(user);
                        userKeyword.setKeyword(keyword);
                        userKeyword.setNotificationEnabled(k.isNotificationEnabled());
                        userKeywordRepository.save(userKeyword);
                    }

                    return KeywordDTO
                            .builder()
                            .id(keyword.getId())
                            .word(keyword.getWord())
                            .notificationEnabled(k.isNotificationEnabled())
                            .build();
                }).collect(Collectors.toList());
    }


    
}
