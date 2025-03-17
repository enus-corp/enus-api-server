/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.enus.newsletter.db.mongodb;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author idohyeon
 */
@Document(collection = "chat_messages")
@CompoundIndex(name = "sessionId_timestamp", def = "{'sessionId': 1, 'timestamp': 1}")
@Builder
@Data
public class ChatMessageDocument {
    @Id
    private String id;
    @Indexed
    private String sessionId;
    private String sender;
    private String content;
    @Indexed
    private LocalDateTime timestamp;
}
