package com.outridernet.launch.websocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendToUser(String username, String destination, Object payload) {

        messagingTemplate.convertAndSendToUser(username, destination, payload);
    }

    public void sendToTopic(String destination, Object payload) {

        messagingTemplate.convertAndSend(destination, payload);
    }

}
