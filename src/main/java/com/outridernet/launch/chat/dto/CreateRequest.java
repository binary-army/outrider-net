package com.outridernet.launch.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateRequest {

    private String message;

    private Double latitude;

    private Double longitude;

    private List<Long> outriderIds;
}