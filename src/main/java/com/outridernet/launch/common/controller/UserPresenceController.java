package com.outridernet.launch.common.controller;

import com.outridernet.launch.common.service.UserPresenceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/me")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UserPresenceController {

    private final UserPresenceService presenceService;

    @PostMapping("/online")
    public ResponseEntity<Void> goOnline(
            Authentication authentication
    ) {

        presenceService.goOnline(authentication.getName());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/offline")
    public ResponseEntity<Void> goOffline(
            Authentication authentication
    ) {

        presenceService.goOffline(authentication.getName());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> getStatus(
            Authentication authentication
    ) {

        boolean online = presenceService.isOnline(authentication.getName());

        return ResponseEntity.ok(online);
    }
}