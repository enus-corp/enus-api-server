package com.enus.newsletter.model.dto;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("ChatMessage")
public class ChatMessage implements Serializable {
    @Id
    private String id;
    private String sender;
    private String content;
    private String timestamp;
}
