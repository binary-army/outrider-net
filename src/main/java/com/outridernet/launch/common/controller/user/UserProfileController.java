package com.outridernet.launch.common.controller.user;

import com.outridernet.launch.common.controller.response.UserResponse;
import com.outridernet.launch.common.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/me")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponse> getMyProfile(
            Authentication authentication
    ) {

        UserResponse response =
                userService.getMyProfile(authentication.getName());

        return ResponseEntity.ok(response);
    }
}