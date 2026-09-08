package com.outridernet.launch.chat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "chat_messages",
        indexes = {
                @Index(
                        name = "idx_chat_message_conversation_created",
                        columnList = "conversation_id, created_at"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conversation_id", nullable = false)
    private Long conversationId;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}