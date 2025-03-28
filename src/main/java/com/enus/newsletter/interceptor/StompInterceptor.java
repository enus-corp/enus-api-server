package com.enus.newsletter.interceptor;

import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.enus.newsletter.exception.auth.AuthErrorCode;
import com.enus.newsletter.exception.auth.AuthException;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "STOMP_INTERCEPTOR")
@Component
public class StompInterceptor implements ChannelInterceptor {
    // ChannelInterceptor -> Intercepts messages that are sent to MessageChannel

    @Override
    @Nullable
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor != null && accessor.getCommand() != null) {
            // check CONNECT command
            if (accessor.getCommand().equals(StompCommand.CONNECT)) {
                log.info("STOMP Command: CONNECT || Message: {}", message);
                String authToken = accessor.getFirstNativeHeader("Authorization");
                if (authToken == null || !(authToken != null && authToken.startsWith("Bearer"))) {
                    throw new AuthException(AuthErrorCode.INVALID_TOKEN, AuthErrorCode.INVALID_TOKEN.getMessage());
                }
            }
        }

        return message;
    }

}
