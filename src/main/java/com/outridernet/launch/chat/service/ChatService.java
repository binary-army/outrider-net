package com.outridernet.launch.chat.service;

import com.outridernet.launch.chat.entity.ChatMessage;
import com.outridernet.launch.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(
            Long conversationId,
            Long senderId,
            String message
    ) {

        ChatMessage chatMessage = ChatMessage.builder()
                .conversationId(conversationId)
                .senderId(senderId)
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();

        return chatMessageRepository.save(chatMessage);
    }
}