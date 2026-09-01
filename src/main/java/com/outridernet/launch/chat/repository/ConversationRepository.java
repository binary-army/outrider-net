package com.outridernet.launch.chat.repository;

import com.outridernet.launch.chat.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository
        extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByRequestId(Long requestId);
}