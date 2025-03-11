package com.enus.newsletter.controller;

import com.enus.newsletter.model.dto.ChatMessage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j(topic = "CHAT_CONTROLLER")
public class ChatController {

    @MessageMapping("/chat")
    public void chat(@Payload ChatMessage message) {
        log.info("Received message: {}", message);
    }
}
