package com.enus.newsletter.model.request.keyword;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class BatchKeywordRequest {
    @NotEmpty(message = "At least one or more keywords must be provided")
    @Valid
    private List<KeywordRequest> keywords;
}
