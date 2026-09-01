package com.outridernet.launch.chat.dto;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String message;

    private Double latitude;

    private Double longitude;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private Long acceptedOutriderId;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}