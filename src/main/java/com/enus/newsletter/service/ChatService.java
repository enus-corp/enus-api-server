package com.enus.newsletter.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.enus.newsletter.db.entity.ChatSessionEntity;
import com.enus.newsletter.db.mongodb.ChatMessageDocument;
import com.enus.newsletter.db.mongodb.IChatMessageRepository;
import com.enus.newsletter.db.repository.imp.IChatSessionRepository;
import com.enus.newsletter.model.dto.ChatMessage;

@Service
public class ChatService {

    // private IRedisRepository redisRepository;
    private IChatSessionRepository chatSessionRepository;
    private IChatMessageRepository chatMessageRepository;
    // private RedisTemplate<String, ChatMessage> redisTemplate;

    public ChatService(
        // IRedisRepository redisRepository, 
        IChatSessionRepository chatSessionRepository, 
        // RedisTemplate<String, ChatMessage> redisTemplate, 
        IChatMessageRepository chatMessageRepository) {

        this.chatMessageRepository = chatMessageRepository;    
        // this.redisRepository = redisRepository;
        this.chatSessionRepository = chatSessionRepository;
        // this.redisTemplate = redisTemplate;
    }

    public ChatMessage saveChatHistory(ChatMessage chat) {
        // Return chat session ID to client
        String chatSessionId = UUID.randomUUID().toString();
        chat.setChatId(chatSessionId);

        // save chat content to mongodb
        ChatMessageDocument chatMessageDocument = ChatMessageDocument.builder()
            .sessionId(chatSessionId)
            .sender(chat.getSender())
            .content(chat.getContent())
            .timestamp(LocalDateTime.now())
            .build();

        chatMessageRepository.save(chatMessageDocument);
        return chat;
    }

    public void saveChatSession(ChatSessionEntity chat) {
        chatSessionRepository.save(chat);
    }
}
