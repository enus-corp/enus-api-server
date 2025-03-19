package com.enus.newsletter.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.enus.newsletter.model.dto.ChatMessage;
import com.enus.newsletter.service.ChatService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j(topic = "CHAT_CONTROLLER")
public class ChatController {
    private ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/request-chat-id")
    @SendTo("/topic/chat")
    public Map<String, String> requestChatId(Message<?> message) {
        log.info("Received request for chat id");
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getSessionId();
        String chatId = UUID.randomUUID().toString();

        Map<String, String> sessionInfo = new HashMap<>();
        sessionInfo.put("chatId", chatId);
        sessionInfo.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        log.info("Sending session info: {}", sessionInfo);
        // messagingTemplate.convertAndSendToUser(sessionId, "/topic/chat", sessionInfo);
        return sessionInfo;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage chat(@Payload ChatMessage message) {
        log.info("Received message: {}", message);
        ChatMessage c = chatService.saveChatHistory(message);
        return c;
    }

    @MessageMapping("/chat/client/{chatId}")
    @SendTo("/topic/server/{chatId}")
    public ChatMessage handleClientMessage(@Payload ChatMessage message) {
        log.info("Received message: {}", message);
        chatService.saveChatHistory(message);
        return message;
    }

    @MessageMapping("/chat/server/{chatId}")
    @SendTo("/topic/client/{chatId}")
    public ChatMessage handleServerMessage(@Payload ChatMessage message) {
        log.info("Received message: {}", message);
        chatService.saveChatHistory(message);
        return message;
    }
}
