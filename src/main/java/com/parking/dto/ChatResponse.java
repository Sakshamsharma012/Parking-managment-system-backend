package com.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for outgoing chat responses from the AI chatbot.
 */
public class ChatResponse {
    private String reply;

    public ChatResponse() {}
    public ChatResponse(String reply) { this.reply = reply; }

    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }
}
