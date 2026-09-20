package com.outridernet.launch.common.service;

import com.outridernet.launch.common.controller.response.UserResponse;
import com.outridernet.launch.common.entity.User;
import com.outridernet.launch.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponse getMyProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return UserResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .profileImage(user.getProfileImage())
                .build();
    }
}