package com.outridernet.launch.chat.repository;

import com.outridernet.launch.chat.dto.ChatRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRequestRepository
        extends JpaRepository<ChatRequest, Long> {
}