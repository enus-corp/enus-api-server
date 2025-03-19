/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.enus.newsletter.handler;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idohyeon
 */
@Component
@Slf4j(topic="WEBSOCKET_HANDLER")
public class WebSocketHandler {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // @EventListener
    // public void handleSessionConnected(SessionConnectedEvent event) {
    //     StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
    //     String sessionId = accessor.getSessionId();
    //     log.info("Client Session: {}", sessionId);

    //     String chatId = UUID.randomUUID().toString();

    //     Map<String, String> sessionInfo = new HashMap<>();
    //     sessionInfo.put("chatId", chatId);
    //     sessionInfo.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    //     log.info("Sending session info: {}", sessionInfo);
    //     messagingTemplate.convertAndSendToUser(sessionId, "/queue/session-info", sessionInfo);

    // }

}
