package tn.esprit.spring.config;

import lombok.Data;

import java.time.Instant;

@Data
public class ErrorResponse {
    private String message;
    private Instant timestamp;

    public ErrorResponse(String message, Instant timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }
}
