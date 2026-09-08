package com.outridernet.launch.chat.dto;

import com.outridernet.launch.chat.entity.ChatMessage;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageResponse {

    private Long id;

    private Long conversationId;

    private Long senderId;

    private String message;

    private LocalDateTime createdAt;

    public static ChatMessageResponse from(ChatMessage chatMessage) {

        return ChatMessageResponse.builder().id(chatMessage.getId()).conversationId(chatMessage.getConversationId()).senderId(chatMessage.getSenderId()).message(chatMessage.getMessage()).createdAt(chatMessage.getCreatedAt()).build();
    }
}