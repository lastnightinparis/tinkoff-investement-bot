package com.itmo.tinkoffinvestementbot.exception.chat;

import com.itmo.tinkoffinvestementbot.exception.ChatException;

public class UnknownUpdateTypeException extends ChatException {

    public UnknownUpdateTypeException(Long chatId) {
        super(chatId);
    }

}
