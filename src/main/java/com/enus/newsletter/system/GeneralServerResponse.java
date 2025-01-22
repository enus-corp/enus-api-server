package com.enus.newsletter.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;

public class GeneralServerResponse<T> {
    @JsonProperty("error")
    private boolean error;

    @JsonProperty("message")
    private String message;

    @JsonProperty("code")
    private int code;

    @JsonProperty("data")
    @Nullable
    private T data;

    public GeneralServerResponse(
            boolean error,
            String message,
            int code,
            @Nullable T data
    ) {
        this.error = error;
        this.message = message;
        this.code = code;
        this.data = data;
    }
}