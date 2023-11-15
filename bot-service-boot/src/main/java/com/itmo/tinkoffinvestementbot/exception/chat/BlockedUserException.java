package com.itmo.tinkoffinvestementbot.exception.chat;

import com.itmo.tinkoffinvestementbot.exception.ChatException;

public class BlockedUserException extends ChatException {

    public BlockedUserException(Long chatId) {
        super(chatId);
    }

    public BlockedUserException(Long chatId, Throwable cause) {
        super(chatId, cause);
    }
}
