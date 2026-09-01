package com.outridernet.launch.chat.service;

import com.outridernet.launch.chat.dto.*;
import com.outridernet.launch.chat.repository.ChatRequestRepository;
import com.outridernet.launch.chat.repository.RequestRecipientRepository;
import com.outridernet.launch.common.entity.User;
import com.outridernet.launch.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatRequestService {

    private final UserRepository userRepository;
    private final ChatRequestRepository rideRequestRepository;
    private final RequestRecipientRepository recipientRepository;

    @Transactional
    public ChatRequest createRequest(String email, CreateRequest request) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        ChatRequest chatRequest = ChatRequest.builder().userId(user.getId()).message(request.getMessage()).latitude(request.getLatitude()).longitude(request.getLongitude()).status(RequestStatus.PENDING).createdAt(LocalDateTime.now()).expiresAt(LocalDateTime.now().plusMinutes(5)).build();

        chatRequest = rideRequestRepository.save(chatRequest);

        for (Long outriderId : request.getOutriderIds()) {

            RequestRecipient recipient = RequestRecipient.builder().requestId(chatRequest.getId()).outriderId(outriderId).status(RecipientStatus.PENDING).build();

            recipientRepository.save(recipient);
        }

        return chatRequest;
    }

    @Transactional
    public void acceptRequest(Long requestId, String email) {

        User outrider = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Long outriderId = outrider.getId();

        // Find request
        ChatRequest request = rideRequestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request is already accepted or closed");
        }

        // Make sure this Outrider was selected
        RequestRecipient recipient = recipientRepository.findByRequestIdAndOutriderId(requestId, outriderId).orElseThrow(() -> new RuntimeException("You are not selected for this request"));

        if (recipient.getStatus() != RecipientStatus.PENDING) {

            throw new RuntimeException("Request is no longer available");
        }

        // Accept
        request.setStatus(RequestStatus.ACCEPTED);
        request.setAcceptedOutriderId(outriderId);

        recipient.setStatus(RecipientStatus.ACCEPTED);

        rideRequestRepository.save(request);
        recipientRepository.save(recipient);
    }

}