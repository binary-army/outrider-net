package com.outridernet.launch.chat.controller;

import com.outridernet.launch.chat.dto.ChatMessageResponse;
import com.outridernet.launch.chat.service.ChatService;
import com.outridernet.launch.common.entity.User;
import com.outridernet.launch.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    private final UserRepository userRepository;

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getMessages(
            @PathVariable Long conversationId,
            Principal principal
    ) {

        User user =
                userRepository
                        .findByEmail(principal.getName())
                        .orElseThrow(
                                () -> new IllegalStateException(
                                        "User not found"
                                )
                        );

        List<ChatMessageResponse> messages =
                chatService.getMessages(
                        conversationId,
                        user.getId()
                );

        return ResponseEntity.ok(messages);
    }
}