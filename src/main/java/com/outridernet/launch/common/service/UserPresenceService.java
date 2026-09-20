package com.outridernet.launch.common.service;


import com.outridernet.launch.common.entity.User;
import com.outridernet.launch.common.entity.UserPresence;
import com.outridernet.launch.common.repository.UserPresenceRepository;
import com.outridernet.launch.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserPresenceService {

    private final UserPresenceRepository presenceRepository;
    private final UserRepository userRepository;

    @Transactional
    public void goOnline(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        UserPresence presence = presenceRepository
                .findByUserId(user.getId())
                .orElseGet(() ->
                        UserPresence.builder()
                                .user(user)
                                .build()
                );

        presence.setOnline(true);
        presence.setLastSeenAt(Instant.now());

        presenceRepository.save(presence);
    }

    @Transactional
    public void goOffline(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        UserPresence presence = presenceRepository
                .findByUserId(user.getId())
                .orElseGet(() ->
                        UserPresence.builder()
                                .user(user)
                                .build()
                );

        presence.setOnline(false);
        presence.setLastSeenAt(Instant.now());

        presenceRepository.save(presence);
    }

    public boolean isOnline(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return presenceRepository
                .findByUserId(user.getId())
                .map(UserPresence::isOnline)
                .orElse(false);
    }
}