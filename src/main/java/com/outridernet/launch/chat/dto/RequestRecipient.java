package com.outridernet.launch.chat.dto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "request_recipients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestRecipient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long requestId;

    private Long outriderId;

    @Enumerated(EnumType.STRING)
    private RecipientStatus status;
}