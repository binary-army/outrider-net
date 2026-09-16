package com.outridernet.launch.chat.repository;

import com.outridernet.launch.chat.entity.Conversation;
import com.outridernet.launch.chat.entity.ConversationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository
        extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByRequestId(
            Long requestId
    );

    Optional<Conversation> findByIdAndStatus(
            Long id,
            ConversationStatus status
    );

    boolean existsByRequestId(Long requestId);
}