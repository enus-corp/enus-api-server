/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.enus.newsletter.db.mongodb;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author idohyeon
 */
@Document(collection = "chat_messages")
public class ChatMessageDocument {
    @Id
    private String id;
    private String sender;
    private String content;
    private LocalDateTime timestamp;
    private String converstaionId;
}
