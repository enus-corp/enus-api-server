package com.enus.newsletter.controller;

import com.enus.newsletter.model.dto.KeywordDTO;
import com.enus.newsletter.model.dto.UserDTO;
import com.enus.newsletter.model.request.keyword.BatchKeywordRequest;
import com.enus.newsletter.model.request.keyword.KeywordRequest;
import com.enus.newsletter.model.request.keyword.UpdateKeywordNotificationRequest;
import com.enus.newsletter.system.IdReq;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.enus.newsletter.service.KeywordService;
import com.enus.newsletter.system.GeneralServerResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;

import java.util.List;


@Tag(name="Keyword", description = "Keyword")
@RestController
@RequestMapping("/api/keyword")
@Slf4j(topic = "KEYWORD_CONTROLLER")
public class KeywordController {
    private final KeywordService keywordService;

    public KeywordController(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    @GetMapping("getKeywords")
    public ResponseEntity<GeneralServerResponse<List<KeywordDTO>>> getKeywords(
            @AuthenticationPrincipal UserDTO currentUser) {
        List<KeywordDTO> keywords = keywordService.getUserKeywords(currentUser.getUserId());
        return ResponseEntity.ok(new GeneralServerResponse<>(
            false, 
            "Successfully returned registered keywords",
            0, 
            keywords));
    }

    @PostMapping("batchAdd")
    public ResponseEntity<GeneralServerResponse<List<KeywordDTO>>> addKeywordsInBatch(
            @AuthenticationPrincipal UserDTO currentUser,
            @Valid @RequestBody BatchKeywordRequest req) {
        log.info("[KeywordController][Add Batch] Adding new keywords in batch");
        List<KeywordDTO> keywords = keywordService.addKeywordsInBatch(currentUser.getUserId(), req.getKeywords());
        return ResponseEntity.ok(new GeneralServerResponse<>(
                false,
                "Successfully saved keywords",
                0,
                keywords));
    }

    @PostMapping("add")
    public ResponseEntity<GeneralServerResponse<KeywordDTO>> addKeyword(
            @AuthenticationPrincipal UserDTO currentUser,
            @Valid @RequestBody KeywordRequest req) {
        log.info("[KeywordController][Add Keyword] Adding new keyword");
        KeywordDTO keyword = keywordService.addKeywordToUser(currentUser.getUserId(), req);
        return ResponseEntity.ok(new GeneralServerResponse<>(
            false,
            "Successfully saved keywords",
            0,
            keyword));
    }

    @DeleteMapping("delete")
    public ResponseEntity<GeneralServerResponse<Void>> deleteKeyword(
            @AuthenticationPrincipal UserDTO currentUser,
            @Valid @RequestBody IdReq keywordId) {
        log.info("[KeywordController][Delete Keyword] Deleting keyword");
        log.info(currentUser.getUserId().toString());
        log.info(keywordId.getId().toString());
        keywordService.removeKeywordFromUser(currentUser.getUserId(), keywordId.getId());
        return ResponseEntity.ok(new GeneralServerResponse<Void>(
            false,
            "Successfully deleted keyword",
            0,
            null));
    }

    @PutMapping("notification")
    public ResponseEntity<GeneralServerResponse<KeywordDTO>> toggleNotification(
            @AuthenticationPrincipal UserDTO currentUser,
            @Valid @RequestBody UpdateKeywordNotificationRequest req) {
        log.info("[KeywordController][Toggle Notification] Toggling notification for keyword");
        KeywordDTO keyword = keywordService.updateKeywordNotification(currentUser.getUserId(), req);
        return ResponseEntity.ok(new GeneralServerResponse<KeywordDTO>(
            false,
            "Successfully toggled notification",
            0,
            keyword));
    }
}
