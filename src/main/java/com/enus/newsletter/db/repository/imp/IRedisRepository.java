package com.enus.newsletter.db.repository.imp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enus.newsletter.model.dto.ChatMessage;


@Repository
public interface IRedisRepository extends CrudRepository<ChatMessage, String> {
    
}
