package com.enus.newsletter.controller;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    @MessageMapping("/init")
    @SendTo("/topic/init")
    public void init() {
        log.info("Init");
        String chatSessionId = UUID.randomUUID().toString();
        // create object to send to client
        HashMap<String, String> map = new HashMap<>();
        map.put("chatSessionId", chatSessionId);
        messagingTemplate.convertAndSend("/topic/init", map);
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
