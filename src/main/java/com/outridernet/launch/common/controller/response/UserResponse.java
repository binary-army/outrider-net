package com.outridernet.launch.common.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private String name;

    private String email;

    private String phone;

    private String profileImage;
}