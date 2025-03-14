package com.enus.newsletter.controller;

import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j(topic = "CHAT_CONTROLLER")
public class ChatController {
    // private ChatService chatService;

    // public ChatController(ChatService chatService) {
    //     this.chatService = chatService;
    // }

    // @MessageMapping("/chat")
    // @SendTo("/topic/messages")
    // public ChatMessage chat(@Payload ChatMessage message) {
    //     log.info("Received message: {}", message);
    //     chatService.saveChatHistory(message);
    //     return message;
    // }
}
