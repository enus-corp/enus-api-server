package com.enus.newsletter.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.enus.newsletter.db.entity.ChatSessionEntity;
import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.db.mongodb.ChatMessageDocument;
import com.enus.newsletter.db.mongodb.IChatMessageRepository;
import com.enus.newsletter.db.repository.imp.IChatSessionRepository;
import com.enus.newsletter.db.repository.imp.IUserRepository;
import com.enus.newsletter.exception.user.UserErrorCode;
import com.enus.newsletter.exception.user.UserException;
import com.enus.newsletter.model.dto.ChatMessage;

@Service
public class ChatService {

    private IChatSessionRepository chatSessionRepository;
    private IChatMessageRepository chatMessageRepository;
    private IUserRepository userRepository;

    public ChatService(
        IChatSessionRepository chatSessionRepository, 
        IChatMessageRepository chatMessageRepository,
        IUserRepository userRepository) {

        this.chatMessageRepository = chatMessageRepository;    
        this.chatSessionRepository = chatSessionRepository;
        this.userRepository = userRepository;
        
    }

    public void saveChatSessionId(String chatId, long userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        ChatSessionEntity entity = new ChatSessionEntity(
            null, // Default ID
            chatId,
            LocalDateTime.now(),
            null,
            user,
            null // TTS Entity not associated
        );
        chatSessionRepository.save(entity);
    }

    public ChatMessage saveChatHistory(ChatMessage chat) {
        // Return chat session ID to client

        // save chat content to mongodb
        ChatMessageDocument chatMessageDocument = ChatMessageDocument.builder()
            .sessionId(chat.getChatId())
            .sender(chat.getSender())
            .content(chat.getContent())
            .timestamp(LocalDateTime.now())
            .build();

        chatMessageRepository.save(chatMessageDocument);
        return chat;
    }

    // public void getchatHistory(String chatId) {
    //     Query query = new Query(Criteria.where("chatId").is(chatId)).with(Sort.by(Sort.Direction.ASC, "timestamp"));
    // }
}
