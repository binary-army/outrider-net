package com.outridernet.launch.chat.repository;

import com.outridernet.launch.chat.dto.RequestRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestRecipientRepository
        extends JpaRepository<RequestRecipient, Long> {

    Optional<RequestRecipient> findByRequestIdAndOutriderId(
            Long requestId,
            Long outriderId
    );
}