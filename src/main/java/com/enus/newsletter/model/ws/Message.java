package com.enus.newsletter.model.ws;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private MessageType type;
    private String content;
    private String sender;
    private String sessionId;
}
