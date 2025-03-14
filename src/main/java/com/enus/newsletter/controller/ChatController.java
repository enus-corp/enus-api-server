package com.enus.newsletter.controller;

import com.enus.newsletter.model.dto.ChatMessage;
import com.enus.newsletter.service.ChatService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j(topic = "CHAT_CONTROLLER")
public class ChatController {
    private ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage chat(@Payload ChatMessage message) {
        log.info("Received message: {}", message);
        chatService.saveChatHistory(message);
        return message;
    }
}
