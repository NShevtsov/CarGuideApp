package com.project.carguideapp.dto;

import java.time.LocalDateTime;

public class ErrorDto {
    private final LocalDateTime timestamp;
    private final String message;

    public ErrorDto(LocalDateTime timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
