package com.outridernet.launch.chat.service;

import com.outridernet.launch.chat.dto.ChatMessageResponse;
import com.outridernet.launch.chat.entity.ChatMessage;
import com.outridernet.launch.chat.entity.Conversation;
import com.outridernet.launch.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    private final ConversationService conversationService;

    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public ChatMessageResponse sendMessage(Long conversationId, Long senderId, String message, String recipientUsername) {

        // 1. Get ACTIVE conversation
        Conversation conversation = conversationService.getActiveConversation(conversationId);

        // 2. Verify sender belongs to conversation
        conversationService.validateParticipant(conversation, senderId);

        // 3. Clean message
        String cleanMessage = message.trim();

        if (cleanMessage.isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }

        // 4. Save message
        ChatMessage chatMessage = ChatMessage.builder().conversationId(conversationId).senderId(senderId).message(cleanMessage).createdAt(LocalDateTime.now()).build();

        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        // 5. Convert to response DTO
        ChatMessageResponse response = ChatMessageResponse.from(savedMessage);

        // 6. Send to recipient
        messagingTemplate.convertAndSendToUser(recipientUsername, "/queue/chat", response);

        return response;
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getMessages(Long conversationId, Long userId) {

        Conversation conversation = conversationService.getActiveConversation(conversationId);

        conversationService.validateParticipant(conversation, userId);

        return chatMessageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId).stream().map(ChatMessageResponse::from).toList();
    }
}