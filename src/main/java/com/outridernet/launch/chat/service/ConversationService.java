package com.outridernet.launch.chat.service;

import com.outridernet.launch.chat.entity.Conversation;
import com.outridernet.launch.chat.entity.ConversationStatus;
import com.outridernet.launch.chat.exception.ChatAccessDeniedException;
import com.outridernet.launch.chat.exception.ConversationNotFoundException;
import com.outridernet.launch.chat.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;

    @Transactional(readOnly = true)
    public Conversation getActiveConversation(Long conversationId) {

        return conversationRepository.findByIdAndStatus(conversationId, ConversationStatus.ACTIVE).orElseThrow(() -> new ConversationNotFoundException(conversationId));
    }

    @Transactional(readOnly = true)
    public void validateParticipant(Conversation conversation, Long userId) {

        boolean participant = conversation.getUserId().equals(userId) || conversation.getOutriderId().equals(userId);

        if (!participant) {
            throw new ChatAccessDeniedException();
        }
    }

    public Long getOtherParticipantId(Conversation conversation, Long senderId) {

        if (conversation.getUserId().equals(senderId)) {

            return conversation.getOutriderId();
        }

        if (conversation.getOutriderId().equals(senderId)) {

            return conversation.getUserId();
        }

        throw new ChatAccessDeniedException();
    }

    @Transactional
    public Conversation createConversation(Long requestId, Long userId, Long outriderId) {

        if (conversationRepository.existsByRequestId(requestId)) {

            return conversationRepository.findByRequestId(requestId).orElseThrow();
        }

        Conversation conversation = Conversation.builder().requestId(requestId).userId(userId).outriderId(outriderId).status(ConversationStatus.ACTIVE).build();

        return conversationRepository.save(conversation);
    }
}