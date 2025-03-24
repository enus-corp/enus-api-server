package com.enus.newsletter.model.request.keyword;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class BatchKeywordRequest {
    @NotEmpty(message = "At least one or more keywords must be provided")
    @Valid
    @JsonProperty("keywords")
    private List<KeywordRequest> keywords;
}
