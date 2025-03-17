package com.enus.newsletter.db.mongodb;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IChatMessageRepository extends MongoRepository<ChatMessageDocument, String>{
}
