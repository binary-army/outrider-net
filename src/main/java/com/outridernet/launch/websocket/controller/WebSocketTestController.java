package com.outridernet.launch.websocket.controller;

import com.outridernet.launch.websocket.service.WebSocketNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WebSocketTestController {

    private final WebSocketNotificationService notificationService;

    @MessageMapping("/test")
    public void test(@Payload String message, Principal principal) {

        System.out.println("WebSocket user: " + principal.getName());

        System.out.println("Message: " + message);

        notificationService.sendToUser(principal.getName(), "/queue/test", message);
    }
}