package com.outridernet.launch.websocket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
@Profile("local")
public class WebSocketTestController {

    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/topic")
    public String sendTopicMessage() {

        messagingTemplate.convertAndSend(
                "/topic/test",
                "Hello from topic!"
        );

        return "Topic message sent";
    }

    @PostMapping("/user/{email}")
    public String sendUserMessage(
            @PathVariable String email) {

        System.out.println("========== SEND USER MESSAGE ==========");
        System.out.println("Target email: [" + email + "]");

        messagingTemplate.convertAndSendToUser(
                email,
                "/queue/chat-request",
                "Hello specific user!"
        );

        System.out.println("Message sent using convertAndSendToUser");
        System.out.println("======================================");

        return "User message sent to " + email;
    }
}