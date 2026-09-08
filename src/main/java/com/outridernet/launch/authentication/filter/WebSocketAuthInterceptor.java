package com.outridernet.launch.authentication.filter;

import com.outridernet.launch.authentication.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        // ==============================
        // CONNECT
        // ==============================
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authorization = accessor.getFirstNativeHeader("Authorization");

            if (authorization == null || !authorization.startsWith("Bearer ")) {

                throw new IllegalArgumentException("Missing WebSocket Authorization header");
            }

            String token = authorization.substring(7);

            String email = jwtService.extractEmail(token);


            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // Set authenticated user
            accessor.setUser(authentication);

            // Store authentication in WebSocket session
            accessor.getSessionAttributes().put("WEBSOCKET_USER", authentication);

        }

        // ==============================
        // SUBSCRIBE / SEND / etc.
        // ==============================
        else {

            Object storedUser = null;

            if (accessor.getSessionAttributes() != null) {
                storedUser = accessor.getSessionAttributes().get("WEBSOCKET_USER");
            }

            if (storedUser instanceof UsernamePasswordAuthenticationToken authentication) {

                accessor.setUser(authentication);
            }
        }

        return message;
    }
}