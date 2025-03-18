package com.enus.newsletter.model.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage implements Serializable {
    // Chat Message sent from publisher to subscriber

    private String chatId;
    private String sender;
    private String content;
}
