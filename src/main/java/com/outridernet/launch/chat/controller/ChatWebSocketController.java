package com.outridernet.launch.chat.controller;

import com.outridernet.launch.chat.dto.ChatMessageRequest;
import com.outridernet.launch.chat.entity.Conversation;
import com.outridernet.launch.chat.service.ChatService;
import com.outridernet.launch.chat.service.ConversationService;
import com.outridernet.launch.common.entity.User;
import com.outridernet.launch.common.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;

    private final ConversationService conversationService;

    private final UserRepository userRepository;

    @MessageMapping("/chat.send")
    public void sendMessage(
            @Valid ChatMessageRequest request,
            Principal principal
    ) {

        if (principal == null) {
            throw new IllegalStateException(
                    "WebSocket user is not authenticated"
            );
        }

        String email = principal.getName();

        User sender =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () -> new IllegalStateException(
                                        "Authenticated user not found"
                                )
                        );

        Conversation conversation =
                conversationService.getActiveConversation(
                        request.getConversationId()
                );

        Long recipientId =
                conversationService.getOtherParticipantId(
                        conversation,
                        sender.getId()
                );

        User recipient =
                userRepository
                        .findById(recipientId)
                        .orElseThrow(
                                () -> new IllegalStateException(
                                        "Recipient not found"
                                )
                        );

        chatService.sendMessage(
                request.getConversationId(),
                sender.getId(),
                request.getMessage(),
                recipient.getEmail()
        );
    }
}
