package com.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for incoming chat requests from the AI chatbot.
 */
public class ChatRequest {
    private String message;

    public ChatRequest() {}
    public ChatRequest(String message) { this.message = message; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
