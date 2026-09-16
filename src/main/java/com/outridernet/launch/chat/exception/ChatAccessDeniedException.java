package com.outridernet.launch.chat.exception;

public class ChatAccessDeniedException extends RuntimeException {

    public ChatAccessDeniedException() {

        super("You are not a participant of this conversation");
    }
}
