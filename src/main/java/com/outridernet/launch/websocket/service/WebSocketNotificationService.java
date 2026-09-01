package com.outridernet.launch.websocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendToUser(String email, String destination, Object message) {

        messagingTemplate.convertAndSendToUser(email, destination, message);
    }
}
