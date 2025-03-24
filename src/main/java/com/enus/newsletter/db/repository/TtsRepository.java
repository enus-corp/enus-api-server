package com.enus.newsletter.db.repository;

import org.springframework.stereotype.Repository;

import com.enus.newsletter.db.AbsBaseRepository;
import com.enus.newsletter.db.entity.ChatSessionEntity;
import com.enus.newsletter.db.entity.TtsEntity;
import com.enus.newsletter.db.repository.imp.IChatSessionRepository;
import com.enus.newsletter.db.repository.imp.ITtsRepository;
import com.enus.newsletter.enums.DetailLevel;
import com.enus.newsletter.enums.ReadSpeed;
import com.enus.newsletter.exception.chat.ChatErrorCode;
import com.enus.newsletter.exception.chat.ChatException;
import com.enus.newsletter.model.request.tts.TtsRequest;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "TTS_REPOSITORY")
@Transactional
@Repository
public class TtsRepository extends AbsBaseRepository<TtsEntity, ITtsRepository> {

    private final IChatSessionRepository chatSessionRepository;

    public TtsRepository(ITtsRepository repository, IChatSessionRepository chatSessionRepository) {
        super(repository);
        this.chatSessionRepository = chatSessionRepository;
    }

    public TtsEntity save(TtsRequest dto) {
        ChatSessionEntity chatSession = chatSessionRepository.findByChatId(dto.getChatId())
            .orElseThrow(() -> new ChatException(ChatErrorCode.CHATID_NOT_FOUND));
        double readSpeed = ReadSpeed.getValueByName(dto.getReadSpeed());
        int detailLevel = DetailLevel.getValueByName(dto.getDetailLevel());
    
        TtsEntity ttsEntity = new TtsEntity(
            null,
            readSpeed,
            detailLevel,
            dto.getBriefingCount(),
            chatSession
        );
        return repository.save(ttsEntity);
    }
    
}
