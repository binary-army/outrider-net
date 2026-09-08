package com.outridernet.launch.websocket.config;

import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
@Profile("local")
public class WebSocketEventListener {

    @EventListener
    public void handleSubscribe(SessionSubscribeEvent event) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        System.out.println("========== SPRING SUBSCRIBE ==========");
        System.out.println("Session ID: " + accessor.getSessionId());
        System.out.println("Destination: " + accessor.getDestination());

        if (accessor.getUser() != null) {
            System.out.println("Principal: " + accessor.getUser().getName());
        } else {
            System.out.println("Principal: NULL");
        }

        System.out.println("======================================");
    }

    @EventListener
    public void handleUnsubscribe(SessionUnsubscribeEvent event) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        System.out.println("========== SPRING UNSUBSCRIBE ==========");
        System.out.println("Session ID: " + accessor.getSessionId());
        System.out.println("Subscription ID: " + accessor.getSubscriptionId());
        System.out.println("=========================================");
    }
}