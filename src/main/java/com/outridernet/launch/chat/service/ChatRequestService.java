package com.outridernet.launch.chat.service;

import com.outridernet.launch.chat.dto.*;
import com.outridernet.launch.chat.entity.Conversation;
import com.outridernet.launch.chat.repository.ChatRequestRepository;
import com.outridernet.launch.chat.repository.RequestRecipientRepository;
import com.outridernet.launch.common.entity.User;
import com.outridernet.launch.common.repository.UserRepository;
import com.outridernet.launch.websocket.service.WebSocketNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatRequestService {

    private final UserRepository userRepository;

    private final ChatRequestRepository chatRequestRepository;

    private final RequestRecipientRepository recipientRepository;

    private final WebSocketNotificationService notificationService;

    private final ConversationService conversationService;


    /**
     * Create ChatRequest and notify all selected nearby users.
     */
    @Transactional
    public ChatRequest createRequest(String email, CreateRequest request) {

        // -----------------------------------------
        // 1. Find sender
        // -----------------------------------------

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));


        // -----------------------------------------
        // 2. Create ChatRequest
        // -----------------------------------------

        LocalDateTime now = LocalDateTime.now();

        ChatRequest chatRequest = ChatRequest.builder().userId(user.getId()).message(request.getMessage()).latitude(request.getLatitude()).longitude(request.getLongitude()).status(RequestStatus.PENDING).createdAt(now).expiresAt(now.plusMinutes(5)).build();


        // -----------------------------------------
        // 3. Save ChatRequest
        // -----------------------------------------

        chatRequest = chatRequestRepository.save(chatRequest);


        // -----------------------------------------
        // 4. Create recipients
        // -----------------------------------------

        for (Long outriderId : request.getOutriderIds()) {

            User outrider = userRepository.findById(outriderId).orElseThrow(() -> new RuntimeException("Outrider not found: " + outriderId));


            // Don't allow sender to send request to himself
            if (outrider.getId().equals(user.getId())) {
                continue;
            }


            RequestRecipient recipient = RequestRecipient.builder().requestId(chatRequest.getId()).outriderId(outriderId).status(RecipientStatus.PENDING).build();


            recipientRepository.save(recipient);


            // -----------------------------------------
            // 5. Real-time notification
            // -----------------------------------------

            notificationService.sendToUser(outrider.getEmail(), "/queue/chat-request", chatRequest);
        }


        return chatRequest;
    }


    /**
     * Accept a ChatRequest.
     */
    @Transactional
    public void acceptRequest(Long requestId, String email) {

        // -----------------------------------------
        // 1. Find accepting user
        // -----------------------------------------

        User outrider = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Long outriderId = outrider.getId();


        // -----------------------------------------
        // 2. Find ChatRequest
        // -----------------------------------------

        ChatRequest request = chatRequestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found"));


        // -----------------------------------------
        // 3. Check request status
        // -----------------------------------------

        if (request.getStatus() != RequestStatus.PENDING) {

            throw new RuntimeException("Request is already accepted or closed");
        }


        // -----------------------------------------
        // 4. Check recipient
        // -----------------------------------------

        RequestRecipient recipient = recipientRepository.findByRequestIdAndOutriderId(requestId, outriderId).orElseThrow(() -> new RuntimeException("You are not selected for this request"));


        // -----------------------------------------
        // 5. Check recipient status
        // -----------------------------------------

        if (recipient.getStatus() != RecipientStatus.PENDING) {

            throw new RuntimeException("Request is no longer available");
        }


        // -----------------------------------------
        // 6. Accept request
        // -----------------------------------------

        request.setStatus(RequestStatus.ACCEPTED);

        request.setAcceptedOutriderId(outriderId);

        recipient.setStatus(RecipientStatus.ACCEPTED);

        // -----------------------------------------
        // 7. create conversion immediate from original sender
        // -----------------------------------------

        Conversation conversation =
                conversationService.createConversation(
                        request.getId(),
                        request.getUserId(),
                        outriderId
                );


        // -----------------------------------------
        // 8. Save
        // -----------------------------------------

        chatRequestRepository.save(request);

        recipientRepository.save(recipient);


        // -----------------------------------------
        // 9. Notify original sender
        // -----------------------------------------

        User sender = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("Sender not found"));


        notificationService.sendToUser(sender.getEmail(), "/queue/chat-request-accepted", request);
    }
}