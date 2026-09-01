package com.outridernet.launch.chat.controller;

import com.outridernet.launch.chat.dto.ChatRequest;
import com.outridernet.launch.chat.dto.CreateRequest;
import com.outridernet.launch.chat.service.ChatRequestService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/chat-requests")
public class ChatController {

    private final ChatRequestService chatRequestService;

    @PostMapping
    public ResponseEntity<ChatRequest> createRequest(Authentication authentication, @RequestBody CreateRequest request) {

        String email = authentication.getName();

        ChatRequest created = chatRequestService.createRequest(email, request);

        return ResponseEntity.ok(created);
    }

    @PostMapping("/{requestId}/accept")
    public ResponseEntity<Void> acceptRequest(@PathVariable Long requestId, Authentication authentication) {

        String email = authentication.getName();

        chatRequestService.acceptRequest(requestId, email);

        return ResponseEntity.ok().build();
    }
}