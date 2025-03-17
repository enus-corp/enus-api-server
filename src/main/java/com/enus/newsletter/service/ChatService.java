package com.enus.newsletter.service;

import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.enus.newsletter.db.entity.ChatSessionEntity;
import com.enus.newsletter.db.repository.imp.IChatSessionRepository;
import com.enus.newsletter.model.dto.ChatMessage;

@Service
public class ChatService {

    // private IRedisRepository redisRepository;
    private IChatSessionRepository chatSessionRepository;
    private RedisTemplate<String, ChatMessage> redisTemplate;

    public ChatService(
        // IRedisRepository redisRepository, 
        IChatSessionRepository chatSessionRepository, RedisTemplate<String, ChatMessage> redisTemplate) {
        // this.redisRepository = redisRepository;
        this.chatSessionRepository = chatSessionRepository;
        this.redisTemplate = redisTemplate;
    }

    public void saveChatHistory(ChatMessage chat) {
        // Return chat session ID to client
        String chatSessionId = UUID.randomUUID().toString();
        chat.setId(chatSessionId);
        // save chat content to mongodb
        chat.getContent();
        // redisRepository.save(chat);
        // redisTemplate.expire(chat.getId(), Duration.ofDays(30));
    }

    public void saveChatSession(ChatSessionEntity chat) {
        chatSessionRepository.save(chat);
    }
}
