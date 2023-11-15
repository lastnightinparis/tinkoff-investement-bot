package com.itmo.tinkoffinvestementbot.exception.chat;

import com.itmo.tinkoffinvestementbot.exception.ChatException;

public class ProcessingException extends ChatException {

    public ProcessingException(Long chatId, String message) {
        super(chatId, message);
    }

    public ProcessingException(Long chatId, String message, Throwable cause) {
        super(chatId, message, cause);
    }

    public String getMasterMessage() {
        return String.format("#processing_error\nChat ID: %d, Message: %s", getChatId(), getMessage());
    }
}
