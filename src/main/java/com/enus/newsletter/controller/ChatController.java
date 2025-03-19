package com.enus.newsletter.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
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
    @SendToUser("/queue/chat")
    public Map<String, String> requestChatId(Message<?> message) {
        log.info("Received request for chat id");
        String chatId = UUID.randomUUID().toString();

        Map<String, String> sessionInfo = new HashMap<>();
        sessionInfo.put("chatId", chatId);
        sessionInfo.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        log.info("Sending session info: {}", sessionInfo);
        return sessionInfo;
    }

    @MessageMapping("/chat")
    public void handleClientMessage(@Payload ChatMessage message) {
        log.info("Received message: {}", message);
        chatService.saveChatHistory(message);
        messagingTemplate.convertAndSend("/topic/ai-requests", message);
        log.info("Forwared message to AI server: {}", message);
    }
}
