package com.itmo.tinkoffinvestementbot.exception.chat;

import com.itmo.tinkoffinvestementbot.exception.ChatException;

public class AccessDeniedException extends ChatException {

    public AccessDeniedException(Long chatId) {
        super(chatId);
    }
}
