package com.parking.controller;

import com.parking.dto.ChatRequest;
import com.parking.dto.ChatResponse;
import com.parking.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for the AI Chatbot feature.
 * Accepts user queries and returns natural language responses.
 */
@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * POST /api/chat
     * Endpoint to process chat messages.
     */
    @PostMapping
    public ResponseEntity<ChatResponse> processChat(@RequestBody ChatRequest request) {
        return ResponseEntity.ok(chatService.processChat(request));
    }
}
