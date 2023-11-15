package com.itmo.tinkoffinvestementbot.exception.chat;

import com.itmo.tinkoffinvestementbot.exception.ChatException;

public class UnknownUserException extends ChatException {

    public UnknownUserException(Long chatId) {
        super(chatId);
    }
}
